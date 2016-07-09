package org.inventivetalent.npclib.registry;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import javassist.ClassPool;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.inventivetalent.mcwrapper.auth.GameProfileWrapper;
import org.inventivetalent.npclib.ClassGenerator;
import org.inventivetalent.npclib.NPCLib;
import org.inventivetalent.npclib.NPCType;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.annotation.NPCInfo;
import org.inventivetalent.npclib.entity.NPCEntity;
import org.inventivetalent.npclib.entity.living.human.EntityPlayer;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.npc.living.human.NPCHumanAbstract;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;

import java.lang.reflect.Constructor;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class NPCRegistry implements Iterable<NPCAbstract> {

	static final Map<NPCInfo, Class> generatedClasses = new HashMap<>();

	private static final Map<String, NPCRegistry> registryMap = new HashMap<>();

	private final Plugin plugin;
	private final Map<UUID, NPCAbstract> npcMap = Maps.newHashMap();

	public NPCRegistry(Plugin plugin) {
		this.plugin = plugin;
		if (registryMap.containsKey(plugin.getName())) {
			throw new IllegalArgumentException("Registry for '" + plugin.getName() + "' already exists");
		}
		registryMap.put(plugin.getName(), this);
	}

	public void destroy(boolean removeNpcs) {
		if (!registryMap.containsKey(plugin.getName())) {
			throw new IllegalStateException("Already destroyed");
		}
		if (removeNpcs) {
			for (UUID uuid : npcMap.keySet()) {
				removeNpc(uuid);
			}
		}
		registryMap.remove(plugin.getName());
	}

	public void destroy() {
		destroy(true);
	}

	public static NPCRegistry getRegistry(Plugin plugin) {
		return registryMap.get(plugin.getName());
	}

	/**
	 * Injects the specified NPC classes, so the entities can be loaded properly by the server
	 *
	 * @param classes classes to inject
	 */
	public static void injectClasses(Class... classes) {
		for (Class clazz : classes) {
			if (clazz == null) { continue; }
			getOrGenerateClass(NPCInfo.of(clazz));
		}
	}

	static Class<?> getOrGenerateClass(NPCInfo npcType) {
		if (generatedClasses.containsKey(npcType)) {
			return generatedClasses.get(npcType);
		}
		ClassPool classPool = ClassPool.getDefault();
		try {
			Class generated = ClassGenerator.generateEntityClass(classPool, npcType);
			generatedClasses.put(npcType, generated);
			if (npcType.getId() != -1) {
				injectEntity(generated, npcType.getId(), npcType.getNPCClassName());
			}// -1 -> special entity, don't inject
			return generated;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static void injectEntity(Class<?> clazz, int id, String name) {
		Class EntityTypes = Reflection.nmsClassResolver.resolveSilent("EntityTypes");
		FieldResolver fieldResolver = new FieldResolver(EntityTypes);

		((Map) fieldResolver.resolveWrapper("c").get(null)).put(name, clazz);
		((Map) fieldResolver.resolveWrapper("d").get(null)).put(clazz, name);
		((Map) fieldResolver.resolveWrapper("f").get(null)).put(clazz, Integer.valueOf(id));
		NPCLib.logger.info("Injected " + clazz.getSimpleName() + " as " + name + " with id " + id);
	}

	/**
	 * Creates and spawns the specified NPC Entity
	 *
	 * @param location {@link Location} to spawn the entity at
	 * @param npcClass NPC-Class to spawn
	 * @param <T>      a NPC class extending {@link NPCAbstract}
	 * @return the spawned NPC Entity
	 */
	public <T extends NPCAbstract> T spawnNPC(Location location, Class<T> npcClass) {
		checkNotNull(location);
		checkNotNull(npcClass);
		try {
			NPCInfo npcInfo = NPCInfo.of(npcClass);
			NPCEntity npcEntity = createEntity(location, npcInfo);
			return wrapAndInitEntity(npcEntity, location, npcInfo, npcClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates and spawns the specified NPC Type
	 *
	 * @param location {@link Location} to spawn the entity at
	 * @param npcType  type of the NPC
	 * @return the spawned NPC Entity
	 */

	public NPCAbstract spawnNPC(Location location, NPCType npcType) {
		return spawnNPC(location, checkNotNull(npcType).getNpcClass());
	}

	/**
	 * Creates and spawns a player NPC entity
	 *
	 * @param location    {@link Location} to spawn the entity at
	 * @param npcClass    NPC-Class to spawn
	 * @param gameProfile {@link GameProfileWrapper} to use for the player
	 * @param <T>         a NPC class extending {@link NPCHumanAbstract}
	 * @return the spawned NPC entity
	 */
	public <T extends NPCHumanAbstract> T spawnPlayerNPC(Location location, Class<T> npcClass, GameProfileWrapper gameProfile) {
		checkNotNull(location);
		checkNotNull(npcClass);
		checkNotNull(gameProfile);
		try {
			NPCInfo npcInfo = NPCInfo.of(npcClass);
			NPCEntity npcEntity = createPlayerEntity(location, npcInfo, gameProfile);
			return wrapAndInitEntity(npcEntity, location, npcInfo, npcClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates and spawns a player NPC entity
	 *
	 * @param location {@link Location} to spawn the entity at
	 * @param npcClass NPC-Class to spawn
	 * @param uuid     {@link UUID} of the player
	 * @param name     Name of the player
	 * @param <T>      a NPC class extending {@link NPCHumanAbstract}
	 * @return the spawned NPC entity
	 */
	public <T extends NPCHumanAbstract> T spawnPlayerNPC(Location location, Class<T> npcClass, UUID uuid, String name) {
		if (uuid == null && Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("UUID and Name cannot both be empty");
		}
		return spawnPlayerNPC(location, npcClass, new GameProfileWrapper(checkNotNull(uuid), name));
	}

	public void registerNpc(NPCAbstract npc) {
		NPCLib.debug("Registered", npc, "with", plugin.getName());
		npcMap.put(npc.getUniqueId(), npc);
	}

	public <T extends NPCAbstract> T removeNpc(T npc) {
		npcMap.remove(checkNotNull(npc).getUniqueId());
		npc.despawn();
		return npc;
	}

	public NPCAbstract removeNpc(UUID uuid) {
		NPCAbstract npc = npcMap.remove(checkNotNull(uuid));
		if (npc != null) { npc.despawn(); }
		return npc;
	}

	public Collection<NPCAbstract> getNpcs() {
		return new ArrayList<>(npcMap.values());
	}

	protected <T extends NPCEntity> T createEntity(Location location, NPCInfo npcInfo) {
		if ("EntityPlayer".equals(npcInfo.getNms())) { throw new IllegalArgumentException("cannot construct EntityPlayer using #createEntity"); }

		Class clazz = getOrGenerateClass(npcInfo);

		try {
			//noinspection unchecked
			Constructor constructor = clazz.getConstructor(Reflection.nmsClassResolver.resolve("World"));
			//noinspection unchecked
			return (T) constructor.newInstance(Minecraft.getHandle(location.getWorld()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected EntityPlayer createPlayerEntity(Location location, NPCInfo npcInfo, GameProfileWrapper gameProfile) {
		Class clazz = getOrGenerateClass(npcInfo);
		try {
			Object minecraftServer = new MethodResolver(Bukkit.getServer().getClass()).resolveWrapper("getServer").invoke(Bukkit.getServer());
			Object worldServer = Minecraft.getHandle(location.getWorld());
			Object interactManager = new ConstructorResolver(Reflection.nmsClassResolver.resolve("PlayerInteractManager")).resolve(new Class[] { Reflection.nmsClassResolver.resolve("World") }).newInstance(worldServer);

			//noinspection unchecked
			Constructor constructor = clazz.getConstructor(Reflection.nmsClassResolver.resolve("MinecraftServer"), Reflection.nmsClassResolver.resolve("WorldServer"), gameProfile.getHandle().getClass(), Reflection.nmsClassResolver.resolve("PlayerInteractManager"));
			return (EntityPlayer) constructor.newInstance(minecraftServer, worldServer, gameProfile.getHandle(), interactManager);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected <T extends NPCAbstract> T wrapAndInitEntity(NPCEntity entity, Location location, NPCInfo npcInfo, Class<T> npcClass) throws Exception {
		//		NPCAbstract npcAbstract = (NPCAbstract) new ConstructorResolver(npcClass).resolveFirstConstructorSilent().newInstance(entity);
		NPCAbstract npcAbstract = entity.getNPC();
		entity.setNpcInfo(npcInfo);
		npcAbstract.postInit(this.plugin, location);
		npcAbstract.spawn();
		//noinspection unchecked
		return (T) npcAbstract;
	}

	@Override
	public Iterator<NPCAbstract> iterator() {
		return npcMap.values().iterator();
	}
}
