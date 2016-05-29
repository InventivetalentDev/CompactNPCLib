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

package org.inventivetalent.npclib.npc;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.entity.NPCEntityInsentient;
import org.inventivetalent.reflection.util.AccessUtil;

import java.util.Collection;

public class NPCInsentientAbstract<N extends NPCEntityInsentient, B extends LivingEntity> extends NPCLivingAbstract<N, B> {

	NPCInsentientAbstract(N npcEntity) {
		super(npcEntity);
	}

	@Override
	public void postInit(Location location) throws Exception {
		clearGoalSelector();
		clearTargetSelector();

		super.postInit(location);
	}

	public Object getGoalSelector() throws ReflectiveOperationException {
		return AccessUtil.setAccessible(Reflection.nmsClassResolver.resolve("EntityInsentient").getDeclaredField("goalSelector")).get(getNpcEntity());
	}

	public Collection getGoalListB() throws ReflectiveOperationException {
		return (Collection) AccessUtil.setAccessible(Reflection.nmsClassResolver.resolve("PathfinderGoalSelector").getDeclaredField("b")).get(getGoalSelector());
	}

	public Collection getGoalListC() throws ReflectiveOperationException {
		return (Collection) AccessUtil.setAccessible(Reflection.nmsClassResolver.resolve("PathfinderGoalSelector").getDeclaredField("c")).get(getGoalSelector());
	}

	public void clearGoalSelector() {
		try {
			getGoalListB().clear();
			getGoalListC().clear();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public Object getTargetSelector() throws ReflectiveOperationException {
		return AccessUtil.setAccessible(Reflection.nmsClassResolver.resolve("EntityInsentient").getDeclaredField("targetSelector")).get(getNpcEntity());
	}

	public Collection getTargetListB() throws ReflectiveOperationException {
		return (Collection) AccessUtil.setAccessible(Reflection.nmsClassResolver.resolve("PathfinderGoalSelector").getDeclaredField("b")).get(getTargetSelector());
	}

	public Collection getTargetListC() throws ReflectiveOperationException {
		return (Collection) AccessUtil.setAccessible(Reflection.nmsClassResolver.resolve("PathfinderGoalSelector").getDeclaredField("c")).get(getTargetSelector());
	}

	public void clearTargetSelector() {
		try {
			getTargetListB().clear();
			getTargetListC().clear();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
}
