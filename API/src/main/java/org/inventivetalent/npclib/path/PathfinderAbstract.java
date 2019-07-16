package org.inventivetalent.npclib.path;

import org.bukkit.Particle;
import org.bukkit.World;
import org.inventivetalent.npclib.ai.AIAbstract;
import org.inventivetalent.npclib.entity.living.human.NPCEntityHuman;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.npc.living.human.NPCHumanAbstract;
import org.inventivetalent.vectors.d3.Vector3DDouble;

public abstract class PathfinderAbstract<N extends NPCAbstract<?, ?>> extends AIAbstract<N> {

	protected final Vector3DDouble target;
	protected final double         speed;

	protected boolean finished;
	protected double  progress;

	private NMSPathEntity  pathEntity;
	private Vector3DDouble currentPoint;

	public PathfinderAbstract(Vector3DDouble target, double speed) {
		super();
		this.target = target;
		this.speed = speed;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.currentPoint == null) {
			// We have no points!
			finished = true;
			return;
		}

		int i = (int) this.progress;
		int current = this.progress >= i ? i : i - 1;
		double d = this.progress - current;
		double d1 = 1 - d;

		//		Vector3DDouble currentPoint = this.currentPoint.add(.5, .5, .5);

		if (d + this.speed < 1) {
			double dx = (currentPoint.getX() - getNpcVector().getX()) * speed;
			double dz = (currentPoint.getZ() - getNpcVector().getZ()) * speed;

			//TODO: do we really need this?
			//						dx += Math.random() / 10;
			//						dz += Math.random() / 10;

			getNpc().getNpcEntity().move(dx, 0, dz);
			if (getNpc() instanceof NPCHumanAbstract) {
				((NPCEntityHuman) getNpc().getNpcEntity()).checkMovement(dx, 0, dz);
			}
			this.progress += this.speed;
		} else {
			double bx = (currentPoint.getX() - getNpcVector().getX()) * d1;
			double bz = (currentPoint.getZ() - getNpcVector().getZ()) * d1;

			pathEntity.nextIndex();
			if (!pathEntity.isFinished()) {
				this.currentPoint = pathEntity.getVector(getNpc().getNpcEntity());

				double d2 = this.speed - d1;

				double dx = bx + (currentPoint.getX() - getNpcVector().getX()) * d2;
				double dy = currentPoint.getY() - getNpcVector().getY();
				double dz = bz + (currentPoint.getZ() - getNpcVector().getZ()) * d2;

				//TODO: do we really need this?
				//								dx += Math.random() / 10;
				//								dz += Math.random() / 10;

				getNpc().getNpcEntity().move(dx, dy, dz);
				if (getNpc() instanceof NPCHumanAbstract) {
					((NPCEntityHuman) getNpc().getNpcEntity()).checkMovement(dx, dy, dz);
				}
				this.progress += this.speed;
			} else {
				getNpc().getNpcEntity().move(bx, 0, bz);
				if (getNpc() instanceof NPCHumanAbstract) {
					((NPCEntityHuman) getNpc().getNpcEntity()).checkMovement(bx, 0, bz);
				}
				finished = true;
			}
		}
	}

	public void find() {
		this.pathEntity = findPath();
		this.currentPoint = this.pathEntity.getVector(getNpc().getNpcEntity());
	}

	protected abstract NMSPathEntity findPath();

	@Override
	public boolean isFinished() {
		return finished;
	}

	public World getNpcWorld() {
		return getNpc().getBukkitEntity().getWorld();
	}

	public Vector3DDouble getNpcVector() {
		return getNpc().getNpcEntity().getLocationVector();
	}

	public Vector3DDouble getTarget() {
		return target;
	}
}
