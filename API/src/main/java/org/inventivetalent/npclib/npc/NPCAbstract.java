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

package org.inventivetalent.npclib.npc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.inventivetalent.npclib.NPCLib;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.ai.AIAbstract;
import org.inventivetalent.npclib.entity.living.human.NPCEntity;
import org.inventivetalent.npclib.watcher.AnnotatedMethodWatcher;
import org.inventivetalent.npclib.watcher.Watch;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.ResolverQuery;
import org.inventivetalent.reflection.util.AccessUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class NPCAbstract<N extends NPCEntity, B extends Entity> {

	private final   N              npcEntity;
	protected final FieldResolver  npcEntityFieldResolver;
	protected final MethodResolver npcEntityMethodResolver;
	protected final FieldResolver  entityFieldResolver  = new FieldResolver(Reflection.nmsClassResolver.resolveSilent("Entity"));
	protected final MethodResolver entityMethodResolver = new MethodResolver(Reflection.nmsClassResolver.resolveSilent("Entity"));

	private final List<AIAbstract> aiList = new ArrayList<>();

	protected NPCAbstract(N npcEntity) {
		this.npcEntity = npcEntity;
		this.npcEntityFieldResolver = new FieldResolver(npcEntity.getClass());
		this.npcEntityMethodResolver = new MethodResolver(npcEntity.getClass());

		setNPCField("$npc", this);

		this.npcEntity.setMethodWatcher(new AnnotatedMethodWatcher(this));

		//		postInit();
	}

	public void postInit(Location location) throws Exception {
		//TODO: Pathfinder

		//		Reflection.nmsClassResolver.resolve("Entity")
		//				.getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class)
		//				.invoke(getNpcEntity(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		getNpcEntity().setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

		spawn();
	}

	public void spawn() {
		//TODO: NPCSpawnEvent
		getNpcEntity().spawn(CreatureSpawnEvent.SpawnReason.CUSTOM);
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

	// Watched

	@Watch("U()")
	public boolean onBaseTick() {
		System.out.println("base tick!");

		tickAI();

		return true;
	}

	@Watch("move(double,double,double)")
	public boolean onMove(double x, double y, double z) {
		System.out.println("x = [" + x + "], y = [" + y + "], z = [" + z + "]");
		//TODO: NPCMoveEvent/NPCMotionEvent
		return true;
	}

	@Watch("die()")
	public boolean onDie() {
		System.out.println("onDie -> NPCAbstract");
		//TODO: EntityDeathEvent
		return true;
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
