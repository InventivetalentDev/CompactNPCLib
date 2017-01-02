package org.inventivetalent.npclib.npc.living.insentient;

import org.bukkit.entity.LivingEntity;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.entity.living.insentient.NPCEntityInsentient;
import org.inventivetalent.npclib.npc.living.NPCLivingAbstract;
import org.inventivetalent.reflection.util.AccessUtil;

import java.util.Collection;

public abstract class NPCInsentientAbstract<N extends NPCEntityInsentient, B extends LivingEntity> extends NPCLivingAbstract<N, B> {

	public NPCInsentientAbstract(N npcEntity) {
		super(npcEntity);
	}

	@Override
	protected void postInit(String pluginName, double x, double y, double z, float yaw, float pitch) throws Exception {
		clearGoalSelector();
		clearTargetSelector();

		super.postInit(pluginName, x, y, z, yaw, pitch);
	}

	public Object getGoalSelector() throws ReflectiveOperationException {
		return AccessUtil.setAccessible(Reflection.nmsClassResolver.resolve("EntityInsentient").getDeclaredField("goalSelector")).get(getNpcEntity());
	}

	public Collection<?> getGoalListB() throws ReflectiveOperationException {
		return (Collection<?>) AccessUtil.setAccessible(Reflection.nmsClassResolver.resolve("PathfinderGoalSelector").getDeclaredField("b")).get(getGoalSelector());
	}

	public Collection<?> getGoalListC() throws ReflectiveOperationException {
		return (Collection<?>) AccessUtil.setAccessible(Reflection.nmsClassResolver.resolve("PathfinderGoalSelector").getDeclaredField("c")).get(getGoalSelector());
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

	public Collection<?> getTargetListB() throws ReflectiveOperationException {
		return (Collection<?>) AccessUtil.setAccessible(Reflection.nmsClassResolver.resolve("PathfinderGoalSelector").getDeclaredField("b")).get(getTargetSelector());
	}

	public Collection<?> getTargetListC() throws ReflectiveOperationException {
		return (Collection<?>) AccessUtil.setAccessible(Reflection.nmsClassResolver.resolve("PathfinderGoalSelector").getDeclaredField("c")).get(getTargetSelector());
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
