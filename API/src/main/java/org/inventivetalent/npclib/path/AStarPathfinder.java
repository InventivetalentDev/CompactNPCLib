package org.inventivetalent.npclib.path;

import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.vectors.d3.Vector3DDouble;
import org.inventivetalent.vectors.d3.Vector3DInt;

import java.util.ArrayList;
import java.util.List;

public class AStarPathfinder<N extends NPCAbstract<?, ?>> extends PathfinderAbstract<N> {

	private final Vector3DInt intTarget;
	private final int         maxIterations;

	private List<PathPoint> currentPath      = new ArrayList<>();
	private int             currentPathIndex = 0;

	public AStarPathfinder(Vector3DDouble target, double speed, int maxIterations) {
		super(target, speed);
		this.intTarget = new Vector3DInt(target);
		this.maxIterations = maxIterations;
	}

	@Override
	public void tick() {
		if (this.finished) { return; }
		super.tick();

		//		if (currentPathIndex < currentPath.size()) {
		//			PathPoint currentPoint = currentPath.get(currentPathIndex);
		//			Vector3DDouble doublePoint = new Vector3DDouble(currentPoint);
		//			((EntityPlayer) getNpc().getNpcEntity()).g(0, 0.9f);
		//			((NPCLivingAbstract) getNpc()).lookAt(new Vector3DDouble(currentPoint.getX(), ((LivingEntity) getNpc().getBukkitEntity()).getEyeLocation().getY(), currentPoint.getZ()));
		//			//			getNpc().getNpcEntity().move(currentPoint.getX(), currentPoint.getY(), currentPoint.getZ());
		//			System.out.println(doublePoint.distanceSquared(getNpcVector()));
		//			if (doublePoint.distanceSquared(getNpcVector()) < targetThresholdSquared) {
		//				currentPathIndex++;
		//			}
		//		}
	}

	@Override
	protected NMSPathEntity findPath() {
		List<NMSPathPoint> list = calculatePoints();
		//		Object[] pointHandles = new Object[list.size()];
		//		for (int i = 0; i < list.size(); i++) {
		//			pointHandles[i] = new NMSPathPoint(list.get(i)).getHandle();
		//		}
		try {
			//			System.out.println(pointHandles);
			//			System.out.println(Arrays.toString(pointHandles));
			//			System.out.println(pointHandles.getClass());
			return new NMSPathEntity(list);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	List<NMSPathPoint> calculatePoints() {
		int iterations = 0;
		List<NMSPathPoint> pathPoints = new ArrayList<>();

		PathPoint currentPoint = new PathPoint(getIntNpcVector());
		PathPoint closestPoint = currentPoint;
		//		double closestDistance = currentPoint.distanceSquared(intTarget)*2;
		while (true) {
			List<PathPoint> neighbours = closestPoint.getNeighbours(getNpcWorld());
			if (neighbours.isEmpty()) {
				// Can't go any further
				return pathPoints;
			}

			double closestDistance = closestPoint.distanceSquared(intTarget) * 2;
			PathPoint closestNeighbour = closestPoint;
			for (PathPoint neighbour : neighbours) {
				double distance = neighbour.distanceSquared(intTarget);
				if (distance < closestDistance) {
					closestDistance = distance;
					closestNeighbour = neighbour;
				}
			}
			closestPoint = closestNeighbour;
			pathPoints.add(new NMSPathPoint(closestNeighbour));

			//			if (closestPoint.distanceSquared(intTarget) < targetThresholdSquared) {
			//				// We're close enough
			//				return pathPoints;
			//			}

			if (this.maxIterations >= 0 && iterations++ >= this.maxIterations) {
				// It's taking too long to find a path
				return pathPoints;
			}
		}
	}

	Vector3DInt getIntNpcVector() {
		return new Vector3DInt(getNpcVector());
	}

}
