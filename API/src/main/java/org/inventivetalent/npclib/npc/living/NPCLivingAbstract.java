package org.inventivetalent.npclib.npc.living;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.inventivetalent.npclib.ObjectContainer;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.SuperSwitch;
import org.inventivetalent.npclib.entity.living.NPCEntityLiving;
import org.inventivetalent.npclib.event.NPCDeathEvent;
import org.inventivetalent.npclib.event.path.NPCPathFinishEvent;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.path.AStarPathfinder;
import org.inventivetalent.npclib.path.PathfinderAbstract;
import org.inventivetalent.npclib.watcher.Watch;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.ResolverQuery;
import org.inventivetalent.vectors.d3.Vector3DDouble;

public abstract class NPCLivingAbstract<N extends NPCEntityLiving, B extends LivingEntity> extends NPCAbstract<N, B> {

	protected FieldResolver  entityLivingFieldResolver  = new FieldResolver(Reflection.nmsClassResolver.resolveSilent("EntityLiving"));
	protected MethodResolver entityLivingMethodResolver = new MethodResolver(Reflection.nmsClassResolver.resolveSilent("EntityLiving"));

	private PathfinderAbstract pathfinder;

	public NPCLivingAbstract(N npcEntity) {
		super(npcEntity);
	}

	@Watch("void die(DamageSource)")
	public void onDie(ObjectContainer<Object> damageSource, SuperSwitch superSwitch) {
		String damageName = Reflection.getDamageSourceName(damageSource.value);
		EntityDamageEvent.DamageCause cause = Reflection.damageSourceToCause(damageName);
		NPCDeathEvent event = new NPCDeathEvent(this, damageName, cause);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			superSwitch.setCancelled(true);
		}
	}

	@Watch("void g(float,float)")
	public void onHeadingMove(ObjectContainer<Float> strafe, ObjectContainer<Float> forward, SuperSwitch superSwitch) {
		//		System.out.println("onHeadingMove -> NPCLivingAbstract");
		//TODO: NPCMoveEvent...
	}

	/**
	 * Moves the entity forward (on the current heading)
	 *
	 * @param forward forward movement amount
	 * @param strafe  strafe movement amount
	 */
	public void moveForward(float forward, float strafe) {
		invokeEntityLivingMethod("g", float.class, float.class, strafe, forward);
	}

	public void setYaw(float yaw) {
		yaw = clampYaw(yaw);
		getNpcEntity().setYaw(yaw);
		entityLivingFieldResolver.resolveWrapper("aO").set(getNpcEntity(), yaw);
	}

	public void setPitch(float pitch) {
		getNpcEntity().setPitch(pitch);
	}

	protected float clampYaw(float yaw) {
		while (yaw < -180.0F) {
			yaw += 360.0F;
		}

		while (yaw >= 180.0F) {
			yaw -= 360.0F;
		}

		return yaw;
	}

	public void lookAt(Vector3DDouble vector) {
		double dx = vector.getX() - getBukkitEntity().getEyeLocation().getX();
		double dy = vector.getY() - getBukkitEntity().getEyeLocation().getY();
		double dz = vector.getZ() - getBukkitEntity().getEyeLocation().getZ();
		double xzd = Math.sqrt(dx * dx + dz * dz);
		double yd = Math.sqrt(xzd * xzd + dy * dy);

		double yaw = Math.toDegrees(Math.atan2(dz, dx)) - 90.0D;
		double pitch = -Math.toDegrees(Math.atan2(dy, yd));

		setYaw((float) yaw);
		setPitch((float) pitch);
	}

	@Override
	public void tickAI() {
		super.tickAI();
		if (this.pathfinder != null) {
			this.pathfinder.tick();
			if (this.pathfinder.isFinished()) {
				this.pathfinder = null;

				Bukkit.getPluginManager().callEvent(new NPCPathFinishEvent(this));
			}
		}
	}

	public void pathfindTo(Vector3DDouble target, double speed) {
		this.pathfinder = new AStarPathfinder(target, speed, 128);
		//noinspection unchecked
		this.pathfinder.setNpc(this);
		this.pathfinder.find();
	}

	public void moveWithHeading(float strafeMotion, float forwardMotion) {
		try {
			entityLivingMethodResolver.resolve(new ResolverQuery("g", float.class, float.class)).invoke(getNpcEntity(), strafeMotion, forwardMotion);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public Object getEntityLivingField(String field) {
		return entityLivingFieldResolver.resolveWrapper(field).get(getNpcEntity());
	}

	public void setEntityLivingField(String field, Object value) {
		entityLivingFieldResolver.resolveWrapper(field).set(getNpcEntity(), value);
	}

	public Object invokeEntityLivingMethod(String method, Class[] types, Object[] args) {
		return entityLivingMethodResolver.resolveWrapper(new ResolverQuery(method, types)).invoke(getNpcEntity(), args);
	}

	public Object invokeEntityLivingMethod(String method, Object... args) {
		return entityLivingMethodResolver.resolveWrapper(method).invoke(getNpcEntity(), args);
	}

}
