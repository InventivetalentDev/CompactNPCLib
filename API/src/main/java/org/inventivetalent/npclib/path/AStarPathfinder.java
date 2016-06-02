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

package org.inventivetalent.npclib.path;

import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.vectors.d3.Vector3DDouble;
import org.inventivetalent.vectors.d3.Vector3DInt;

import java.util.ArrayList;
import java.util.List;

public class AStarPathfinder<N extends NPCAbstract> extends PathfinderAbstract<N> {

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
