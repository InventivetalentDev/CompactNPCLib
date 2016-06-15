package org.inventivetalent.npclib.npc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;
import org.inventivetalent.boundingbox.BoundingBox;
import org.inventivetalent.npclib.NPCLib;
import org.inventivetalent.npclib.NPCType;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.ai.AIAbstract;
import org.inventivetalent.npclib.entity.NPCEntity;
import org.inventivetalent.npclib.event.NPCDeathEvent;
import org.inventivetalent.npclib.event.NPCSpawnEvent;
import org.inventivetalent.npclib.watcher.AnnotatedMethodWatcher;
import org.inventivetalent.npclib.watcher.Watch;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.ResolverQuery;
import org.inventivetalent.reflection.util.AccessUtil;
import org.inventivetalent.vectors.d3.Vector3DDouble;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class NPCAbstract<N extends NPCEntity, B extends Entity> {

	protected final FieldResolver  npcEntityFieldResolver;
	protected final MethodResolver npcEntityMethodResolver;
	protected final FieldResolver  entityFieldResolver  = new FieldResolver(Reflection.nmsClassResolver.resolveSilent("Entity"));
	protected final MethodResolver entityMethodResolver = new MethodResolver(Reflection.nmsClassResolver.resolveSilent("Entity"));
	private final N npcEntity;
	private final List<AIAbstract> aiList = new ArrayList<>();
	protected Plugin plugin;

	protected NPCAbstract(N npcEntity) {
		this.npcEntity = npcEntity;
		this.npcEntityFieldResolver = new FieldResolver(npcEntity.getClass());
		this.npcEntityMethodResolver = new MethodResolver(npcEntity.getClass());

		setNPCField("$npc", this);

		this.npcEntity.setMethodWatcher(new AnnotatedMethodWatcher(this));

		//		postInit();
	}

	public void postInit(Plugin plugin, Location location) throws Exception {
		this.plugin = plugin;
		//TODO: Pathfinder

		//		Reflection.nmsClassResolver.resolve("Entity")
		//				.getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class)
		//				.invoke(getNpcEntity(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		getNpcEntity().setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

		spawn();
	}

	public void spawn() {
		NPCSpawnEvent event = new NPCSpawnEvent(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		Bukkit.getPluginManager().callEvent(event);
		getNpcEntity().spawn(event.getSpawnReason());
	}

	public <A extends NPCAbstract<N, B>> boolean registerAI(AIAbstract<A> aiAbstract) {
		return aiList.add(aiAbstract);
	}

	public void tickAI() {
		for (Iterator<AIAbstract> iterator = aiList.iterator(); iterator.hasNext(); ) {
			AIAbstract next = iterator.next();
			next.tick();
			if (next.isFinished()) {
				iterator.remove();
			}
		}
	}

	public BoundingBox getAbsoluteBoundingBox() {
		return BoundingBox.fromNMS(getEntityField("boundingBox"));
	}

	public BoundingBox getBoundingBox() {
		return getAbsoluteBoundingBox().translate(new Vector3DDouble(getBukkitEntity().getLocation()).multiply(-1));
	}

	// NPCInfo
	public NPCType getNpcType() {
		return NPCType.forEntityType(getNpcEntity().getNpcInfo().getType());
	}

	public EntityType getEntityType() {
		return getNpcEntity().getNpcInfo().getType();
	}

	// Watched

	@Watch("m()")
	public boolean onBaseTick() {
		//		System.out.println("base tick!");

		tickAI();

		return true;
	}

	@Watch("move(double,double,double)")
	public boolean onMove(double x, double y, double z) {
		//TODO: NPCMoveEvent/NPCMotionEvent
		return true;
	}

	@Watch("die()")
	public boolean onDie() {
		System.out.println("onDie -> NPCAbstract");
		NPCDeathEvent event = new NPCDeathEvent(this, null, null);
		Bukkit.getPluginManager().callEvent(event);
		return !event.isCancelled();
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public N getNpcEntity() {
		return npcEntity;
	}

	public B getBukkitEntity() {
		return (B) invokeNPCMethod("getBukkitEntity");
	}

	public Object getNPCField(String field) {
		return npcEntityFieldResolver.resolveWrapper(field).get(this.npcEntity);
	}

	public void setNPCField(String field, Object value) {
		npcEntityFieldResolver.resolveWrapper(field).set(this.npcEntity, value);
	}

	public Object invokeNPCMethod(String method, Class[] types, Object[] args) {
		return npcEntityMethodResolver.resolveWrapper(new ResolverQuery(method, types)).invoke(this.npcEntity, args);
	}

	public Object invokeNPCMethod(String method, Object... args) {
		return npcEntityMethodResolver.resolveWrapper(method).invoke(this.npcEntity, args);
	}

	public Object getEntityField(String field) {
		return entityFieldResolver.resolveWrapper(field).get(this.npcEntity);
	}

	public void setEntityField(String field, Object value) {
		entityFieldResolver.resolveWrapper(field).set(this.npcEntity, value);
	}

	public Object invokeEntityMethod(String method, Class[] types, Object[] args) {
		return entityMethodResolver.resolveWrapper(new ResolverQuery(method, types)).invoke(this.npcEntity, args);
	}

	public Object invokeEntityMethod(String method, Object... args) {
		return entityMethodResolver.resolveWrapper(method).invoke(this.npcEntity, args);
	}

	protected void broadcastGlobalPacket(Object packet) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			sendPacket(player, packet);
		}
	}

	protected void broadcastPacket(Object packet) {
		for (Player player : getBukkitEntity().getWorld().getPlayers()) {
			sendPacket(player, packet);
		}
	}

	protected void sendPacket(Player player, Object packet) {
		if (player == null || packet == null) { return; }
		if (player == this.getNpcEntity() || player == this.getBukkitEntity()) { return; }
		if (NPCLib.isNPC(player)) { return; }
		try {
			Object handle = Minecraft.getHandle(player);
			if (handle != null) {
				Object connection = AccessUtil.setAccessible(handle.getClass().getDeclaredField("playerConnection")).get(handle);
				if (connection != null) {
					new MethodResolver(connection.getClass()).resolve("sendPacket").invoke(connection, packet);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
