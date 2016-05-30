/*
 * Copyright 2016 inventivetalent.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
 */

package org.inventivetalent.npclib.registry;

import javassist.ClassPool;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.inventivetalent.mcwrapper.auth.GameProfileWrapper;
import org.inventivetalent.npclib.ClassGenerator;
import org.inventivetalent.npclib.NPCLib;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.annotation.NPCInfo;
import org.inventivetalent.npclib.entity.NPCEntity;
import org.inventivetalent.npclib.entity.living.EntityPlayer;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.npc.living.human.NPCHumanAbstract;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class NPCRegistry {

	static final Map<NPCInfo, Class> generatedClasses = new HashMap<>();

	@Getter final Plugin plugin;

	/**
	 * Creates and spawns the specified NPC Entity
	 *
	 * @param location {@link Location} to spawn the entity at
	 * @param npcClass NPC-Class to spawn
	 * @param <T>      a NPC class extending {@link NPCAbstract}
	 * @return the spawned NPC Entity
	 */
	public <T extends NPCAbstract> T createNPC(Location location, Class<T> npcClass) {
		try {
			NPCInfo npcInfo = NPCInfo.of(npcClass);
			NPCEntity npcEntity = createEntity(location, npcInfo);
			return wrapAndInitEntity(npcEntity, location, npcClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
	public <T extends NPCHumanAbstract> T createPlayerNPC(Location location, Class<T> npcClass, GameProfileWrapper gameProfile) {
		try {
			NPCInfo npcInfo = NPCInfo.of(npcClass);
			NPCEntity npcEntity = createPlayerEntity(location, npcInfo, gameProfile);
			return wrapAndInitEntity(npcEntity, location, npcClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

	protected <T extends NPCAbstract> T wrapAndInitEntity(NPCEntity entity, Location location, Class<T> npcClass) throws Exception {
		NPCAbstract npcAbstract = (NPCAbstract) new ConstructorResolver(npcClass).resolveFirstConstructorSilent().newInstance(entity);
		npcAbstract.postInit(location);
		//noinspection unchecked
		return (T) npcAbstract;
	}

	Class<?> getOrGenerateClass(NPCInfo npcType) {
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

	void injectEntity(Class<?> clazz, int id, String name) {
		Class EntityTypes = Reflection.nmsClassResolver.resolveSilent("EntityTypes");
		FieldResolver fieldResolver = new FieldResolver(EntityTypes);

		((Map) fieldResolver.resolveWrapper("c").get(null)).put(name, clazz);
		((Map) fieldResolver.resolveWrapper("d").get(null)).put(clazz, name);
		((Map) fieldResolver.resolveWrapper("f").get(null)).put(clazz, Integer.valueOf(id));
		NPCLib.logger.info("Injected " + clazz.getSimpleName() + " as " + name + " with id " + id);
	}

}
