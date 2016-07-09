/*
 * Copyright 2015-2016 inventivetalent. All rights reserved.
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

package org.inventivetalent.npclib.equipment;

import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.reflection.minecraft.Minecraft;

public enum EquipmentSlot {

	@Deprecated HAND(0),
	MAIN_HAND(0),
	OFF_HAND(1),
	FEET(2),
	LEGS(3),
	CHEST(4),
	HEAD(5);

	private int id;

	EquipmentSlot(int id) {
		this.id = id;
	}

	public int getID() {
		if (Minecraft.VERSION.newerThan(Minecraft.Version.v1_9_R1)) {
			return this.id;
		} else {
			switch (this) {
				case OFF_HAND:
					return 0;
				case FEET:
					return 1;
				case LEGS:
					return 2;
				case CHEST:
					return 3;
				case HEAD:
					return 4;
				default:
					return this.id;
			}
		}
	}

	public Object toNMS() {
		if (Minecraft.VERSION.newerThan(Minecraft.Version.v1_9_R1)) {
			try {
				return Reflection.nmsClassResolver.resolve("EnumItemSlot").getEnumConstants()[this.id];
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		} else if (Minecraft.VERSION.newerThan(Minecraft.Version.v1_8_R1)) {
			return getID();
		} else {
			return null;
		}
	}

}
