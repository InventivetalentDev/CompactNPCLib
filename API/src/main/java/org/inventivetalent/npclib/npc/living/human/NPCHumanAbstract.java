package org.inventivetalent.npclib.npc.living.human;

import org.bukkit.entity.HumanEntity;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.entity.living.human.NPCEntityHuman;
import org.inventivetalent.npclib.npc.living.NPCLivingAbstract;
import org.inventivetalent.npclib.skin.ISkinnableEntity;
import org.inventivetalent.npclib.skin.SkinLayer;
import org.inventivetalent.reflection.minecraft.DataWatcher;

public abstract class NPCHumanAbstract<N extends NPCEntityHuman, B extends HumanEntity> extends NPCLivingAbstract<N, B> implements ISkinnableEntity {

	public NPCHumanAbstract(N npcEntity) {
		super(npcEntity);
	}

	@Override
	public void setSkinLayers(SkinLayer... visibleLayers) {
		int i = 0;
		for (SkinLayer l : visibleLayers) {
			i |= l.getShifted();
		}

		try {
			DataWatcher.setValue(Reflection.getDataWatcher(getBukkitEntity()), 10, DataWatcher.V1_9.ValueType.ENTITY_HUMAN_SKIN_LAYERS, (byte) i);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
