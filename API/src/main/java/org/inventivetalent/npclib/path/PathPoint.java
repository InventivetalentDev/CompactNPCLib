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
