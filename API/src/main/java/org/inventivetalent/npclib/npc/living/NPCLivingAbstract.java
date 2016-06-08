package org.inventivetalent.npclib.npc.living;

import org.bukkit.entity.LivingEntity;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.entity.living.NPCEntityLiving;
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

	protected NPCLivingAbstract(N npcEntity) {
		super(npcEntity);
	}

	@Watch("die(DamageSource)")
	public boolean onDie(Object damageSource) {
		System.out.println("onDie -> NPCLivingAbstract");
		//TODO: NPCDeathEvent (with damage source)
		return true;
	}

	@Watch("g(float,float)")
	public boolean onHeadingMove(float strafe, float forward) {
//		System.out.println("onHeadingMove -> NPCLivingAbstract");
		//TODO: NPCMoveEvent...
		return true;
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
				//TODO: PathFinishEvent
				this.pathfinder = null;
			}
		}
	}

	public void pathfindTo(Vector3DDouble target,double speed) {
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
