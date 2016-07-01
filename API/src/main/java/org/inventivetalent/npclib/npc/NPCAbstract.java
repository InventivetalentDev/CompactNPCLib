package org.inventivetalent.npclib.npc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.inventivetalent.boundingbox.BoundingBox;
import org.inventivetalent.nbt.CompoundTag;
import org.inventivetalent.npclib.*;
import org.inventivetalent.npclib.ai.AIAbstract;
import org.inventivetalent.npclib.entity.NPCEntity;
import org.inventivetalent.npclib.event.*;
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

	@Watch("void m()")
	public void onBaseTick(SuperSwitch superSwitch) {
		tickAI();
	}

	@Watch("void move(double,double,double)")
	public void onMove(ObjectContainer<Double> x, ObjectContainer<Double> y, ObjectContainer<Double> z, SuperSwitch superSwitch) {
		//TODO: NPCMoveEvent/NPCMotionEvent
	}

	@Watch("boolean damageEntity(DamageSource,float)")
	public Boolean onDamage(ObjectContainer<Object> damageSource, ObjectContainer<Float> amount, SuperSwitch superSwitch) {
		System.out.println("onDamage: damageSource = [" + damageSource + "], amount = [" + amount + "]");
		String sourceName = Reflection.getDamageSourceName(damageSource.value);
		EntityDamageEvent.DamageCause damageCause = Reflection.damageSourceToCause(sourceName);
		Entity damager = Reflection.getEntityFromDamageSource(damageSource.value);
		NPCDamageEvent event = new NPCDamageEvent(this, sourceName, damageCause, amount.value, damager);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			superSwitch.setCancelled(true);
			return false;
		}
		amount.value = event.getAmount();
		return !event.isCancelled();
	}

	@Watch("void die()")
	public void onDie(SuperSwitch superSwitch) {
		System.out.println("onDie -> NPCAbstract");
		NPCDeathEvent event = new NPCDeathEvent(this, null, null);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			superSwitch.setCancelled(true);
		}
		System.out.println(superSwitch);
	}

	//NBT

	@Watch("* e(NBTTagCompound)")
	public void onNBTWrite(final ObjectContainer<Object> nbtTagCompound) {
		System.out.println("onNBTWrite ");
		System.out.println(nbtTagCompound.value);

		try {
			CompoundTag compoundTag = new CompoundTag().fromNMS(nbtTagCompound.value);
			NBTWriteEvent event = new NBTWriteEvent(this, nbtTagCompound.value, compoundTag);
			Bukkit.getPluginManager().callEvent(event);

			// Just replacing the nbtTagCompound.value would be easier,
			// but that seems to completely mess up the NBT compound and only leaves the 'id' field.
			// Merging works fine apparently.
			Reflection.mergeNBTCompound(nbtTagCompound.value, compoundTag.toNMS());
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Failed to convert NBTWrite compound", e);
		}
	}

	@Watch("void f(NBTTagCompound)")
	public void onNBTRead(ObjectContainer<Object> nbtTagCompound) {
		System.out.println("onNBTRead ");
		System.out.println(nbtTagCompound.value);

		try {
			CompoundTag compoundTag = new CompoundTag().fromNMS(nbtTagCompound.value);
			NBTReadEvent event = new NBTReadEvent(this, nbtTagCompound.value, compoundTag);
			Bukkit.getPluginManager().callEvent(event);
			// ^ See note above
			Reflection.mergeNBTCompound(nbtTagCompound.value, compoundTag.toNMS());
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Failed to convert NBTRead compound", e);
		}
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
