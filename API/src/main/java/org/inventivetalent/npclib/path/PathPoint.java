package org.inventivetalent.npclib.path;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.inventivetalent.vectors.d3.Vector3DDouble;
import org.inventivetalent.vectors.d3.Vector3DInt;

import java.util.ArrayList;
import java.util.List;

public class PathPoint extends Vector3DInt {

	public PathPoint(Vector3DDouble doubleVector) {
		super(doubleVector);
	}

	public PathPoint(Vector3DInt intVector) {
		this(intVector.getX(), intVector.getY(), intVector.getZ());
	}

	public PathPoint(int x, int y, int z) {
		super(x, y, z);
	}

	public List<PathPoint> getNeighbours(World world) {
		List<PathPoint> list = new ArrayList<>();
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				for (int y = -1; y <= 1; y++) {
					PathPoint point = new PathPoint(this.add(x, y, z));
					if (point.equals(this)) { continue; }
					if (!point.canWalkIn(world)) { continue; }
					if (!point.canWalkOn(world)) { continue; }
					list.add(point);
				}
			}
		}
		return list;
	}

	public Block getBlock(World world) {
		return world.getBlockAt(getX(), getY(), getZ());
	}

	public Block getBlockBelow(World world) {
		return world.getBlockAt(getX(), getY() - 1, getZ());
	}

	public boolean canWalkIn(World world) {
		Block block = getBlock(world);
		Material type = block.getType();
		if (type == Material.AIR) { return true; }
		if (type == Material.WATER || type == Material.STATIONARY_WATER) { return true; }
		if (type == Material.LAVA || type == Material.STATIONARY_LAVA) { return true; }
		if(!type.isSolid())return true;
		return false;
	}

	public boolean canWalkOn(World world) {
		Block block = getBlockBelow(world);
		Material type = block.getType();
		if (type == Material.AIR) { return false; }
		if (type == Material.WATER || type == Material.STATIONARY_WATER) { return false; }
		if (type == Material.LAVA || type == Material.STATIONARY_LAVA) { return false; }
		if(!type.isSolid())return false;
		return true;
	}

}
