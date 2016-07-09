package org.inventivetalent.npclib.npc;

import com.google.common.base.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.inventivetalent.boundingbox.BoundingBox;
import org.inventivetalent.nbt.*;
import org.inventivetalent.nbt.annotation.AnnotatedNBTHandler;
import org.inventivetalent.nbt.annotation.NBT;
import org.inventivetalent.npclib.*;
import org.inventivetalent.npclib.ai.AIAbstract;
import org.inventivetalent.npclib.annotation.NPCInfo;
import org.inventivetalent.npclib.entity.NPCEntity;
import org.inventivetalent.npclib.event.*;
import org.inventivetalent.npclib.event.nbt.NBTReadEvent;
import org.inventivetalent.npclib.event.nbt.NBTWriteEvent;
import org.inventivetalent.npclib.registry.NPCRegistry;
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
import java.util.UUID;
import java.util.logging.Level;

public abstract class NPCAbstract<N extends NPCEntity, B extends Entity> {

	protected final FieldResolver  npcEntityFieldResolver;
	protected final MethodResolver npcEntityMethodResolver;
	protected final FieldResolver  entityFieldResolver  = new FieldResolver(Reflection.nmsClassResolver.resolveSilent("Entity"));
	protected final MethodResolver entityMethodResolver = new MethodResolver(Reflection.nmsClassResolver.resolveSilent("Entity"));
	private final N npcEntity;
	private final List<AIAbstract> aiList = new ArrayList<>();
	protected String              pluginName;
	protected AnnotatedNBTHandler nbtHandler;

	@NBT(value = { "npclib.pluginData" },
		 type = TagID.TAG_COMPOUND)
	private CompoundTag nbtData = new CompoundTag();

	public NPCAbstract(N npcEntity) {
		this.npcEntity = npcEntity;
		this.npcEntityFieldResolver = new FieldResolver(npcEntity.getClass());
		this.npcEntityMethodResolver = new MethodResolver(npcEntity.getClass());

		this.npcEntity.setMethodWatcher(new AnnotatedMethodWatcher(this));
		this.nbtHandler = new AnnotatedNBTHandler(this);
		//		postInit();
	}

	public void postInit(Plugin plugin, Location location) throws Exception {
		this.postInit(plugin.getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}

	protected void postInit(String pluginName, double x, double y, double z, float yaw, float pitch) throws Exception {
		if (this.pluginName != null) {
			this.getPlugin().getLogger().warning("[NPCLib] Attempt to change the NPCs plugin from " + this.pluginName + " to " + pluginName);
		} else {
			this.pluginName = pluginName;
		}
		//TODO: Pathfinder

		NPCRegistry.getRegistry(getPlugin()).registerNpc(this);

		//		Reflection.nmsClassResolver.resolve("Entity")
		//				.getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class)
		//				.invoke(getNpcEntity(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		getNpcEntity().setLocation(x, y, z, yaw, pitch);
	}

	public CompoundTag getNbtData() {
		return nbtData;
	}

	public UUID getUniqueId() {
		return getBukkitEntity().getUniqueId();
	}

	public void spawn() {
		NPCSpawnEvent event = new NPCSpawnEvent(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		Bukkit.getPluginManager().callEvent(event);
		getNpcEntity().spawn(event.getSpawnReason());
	}

	public void despawn() {
		NPCDeathEvent event = new NPCDeathEvent(this, null, null);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) { return; }
		getBukkitEntity().remove();
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

	public void setName(String name) {
		getBukkitEntity().setCustomName(name);
	}

	public void setNameVisible(boolean visible) {
		getBukkitEntity().setCustomNameVisible(visible);
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

	public void setMotion(double x, double y, double z) {
		getNpcEntity().setMotX(x);
		getNpcEntity().setMotY(y);
		getNpcEntity().setMotZ(z);
	}

	@Watch("void move(double,double,double)")
	public void onMove(ObjectContainer<Double> x, ObjectContainer<Double> y, ObjectContainer<Double> z, SuperSwitch superSwitch) {
	}

	@Watch("void g(double,double,double)")
	public void onMotion(ObjectContainer<Double> x, ObjectContainer<Double> y, ObjectContainer<Double> z, SuperSwitch superSwitch) {
		NPCVelocityEvent event = new NPCVelocityEvent(this, x.value, y.value, z.value);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			superSwitch.setCancelled(true);
			return;
		}
		x.value = event.getX();
		y.value = event.getY();
		z.value = event.getZ();
	}

	@Watch("void collide(Entity)")
	public void onCollide(ObjectContainer<Object> entity) {
		Entity bukkitEntity;
		try {
			bukkitEntity = Minecraft.getBukkitEntity(entity.value);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		NPCCollisionEvent event = new NPCCollisionEvent(this, bukkitEntity);
		Bukkit.getPluginManager().callEvent(event);
	}

	@Watch("boolean damageEntity(DamageSource,float)")
	public Boolean onDamage(ObjectContainer<Object> damageSource, ObjectContainer<Float> amount, SuperSwitch superSwitch) {
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
		NPCDeathEvent event = new NPCDeathEvent(this, null, null);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			superSwitch.setCancelled(true);
		}
	}

	//NBT

	@Watch("* e(NBTTagCompound)")
	public void onNBTWrite(final ObjectContainer<Object> nbtTagCompound) {
		NPCLib.debug("Writing", this.getClass().getName(), "to NBT");

		CompoundTag compoundTag = null;
		try {
			compoundTag = new CompoundTag().fromNMS(nbtTagCompound.value);
			writeToNBT(compoundTag);
			NBTWriteEvent event = new NBTWriteEvent(this, nbtTagCompound.value, compoundTag);
			Bukkit.getPluginManager().callEvent(event);

			// Just replacing the nbtTagCompound.value would be easier,
			// but that seems to completely mess up the NBT compound and only leaves the 'id' field.
			// Merging works fine apparently.
			Reflection.mergeNBTCompound(nbtTagCompound.value, compoundTag.toNMS());
		} catch (ReflectiveOperationException e) {
			NPCLib.debug(nbtTagCompound.value);
			NPCLib.debug(compoundTag);
			NPCLib.logger.log(Level.SEVERE, "Exception in NBTWrite", e);
		}
	}

	@Watch("void f(NBTTagCompound)")
	public void onNBTRead(ObjectContainer<Object> nbtTagCompound) {
		NPCLib.debug("Reading", this.getClass().getName(), "from NBT");

		CompoundTag compoundTag = null;
		try {
			compoundTag = new CompoundTag().fromNMS(nbtTagCompound.value);
			readFromNBT(compoundTag);
			NBTReadEvent event = new NBTReadEvent(this, nbtTagCompound.value, compoundTag);
			Bukkit.getPluginManager().callEvent(event);
			// ^ See note above
			Reflection.mergeNBTCompound(nbtTagCompound.value, compoundTag.toNMS());
		} catch (ReflectiveOperationException e) {
			NPCLib.debug(nbtTagCompound.value);
			NPCLib.debug(compoundTag);
			NPCLib.logger.log(Level.SEVERE, "Exception in NBTRead", e);
		}
	}

	public void writeToNBT(CompoundTag compoundTag) {
		NPCType npcType = getNpcType();
		compoundTag.set("npclib.type", npcType.name());
		compoundTag.set("npclib.class", getNpcEntity().getNpcInfo().getNpcClass().getName());
		compoundTag.set("npclib.plugin", this.pluginName);

		this.nbtHandler.onWrite(compoundTag);
	}

	public void readFromNBT(CompoundTag compoundTag) {
		if (compoundTag.has("npclib.plugin")) {
			String pluginName = ((StringTag) compoundTag.get("npclib.plugin")).getValue();
			if (this.pluginName != null) {
				if (!this.pluginName.equals(pluginName)) {
					getPlugin().getLogger().warning("[NPCLib] Tried to load plugin from NBT (" + pluginName + "), but it's already set to " + this.pluginName);
					pluginName = null;
				}
			}
			if (pluginName != null) {
				String className = compoundTag.getString("npclib.class");
				try {
					getNpcEntity().setNpcInfo(NPCInfo.of(Class.forName(className)));
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Could not find NPCClass " + className + " loaded from NBT", e);
				}

				ListTag<DoubleTag> posList = compoundTag.getList("Pos", DoubleTag.class);
				ListTag<FloatTag> rotationList = compoundTag.getList("Rotation", FloatTag.class);

				try {
					this.postInit(pluginName, posList.get(0).getValue(), posList.get(1).getValue(), posList.get(2).getValue(), rotationList.get(0).getValue(), rotationList.get(1).getValue());
				} catch (Exception e) {
					throw new RuntimeException("Failed to postInit " + this.getNpcType() + " from NBT", e);
				}
			}
		}

		this.nbtHandler.onRead(compoundTag);
	}

	public void updateToPlayer(final Player player) {
	}

	public void respawnTo(Player player) {
	}

	public void updateNearby(double radius, Predicate<Player> predicate) {
		double radiusSquared = radius * radius;
		for (Player player : getBukkitEntity().getWorld().getPlayers()) {
			if (player.getLocation().distanceSquared(getBukkitEntity().getLocation()) < radiusSquared) {
				if (predicate == null || predicate.apply(player)) {
					updateToPlayer(player);
				}
			}
		}
	}

	public void updateNearby(double radius) {
		updateNearby(radius, null);
	}

	public void updateNearby() {
		updateNearby(32);
	}

	public Plugin getPlugin() {
		return Bukkit.getPluginManager().getPlugin(this.pluginName);
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

	@Override
	public String toString() {
		return getClass().getName() + "(Id:" + getUniqueId() + ",Name:\"" + getBukkitEntity().getName() + "\",Plugin:" + pluginName + ")@" + hashCode();
	}
}
