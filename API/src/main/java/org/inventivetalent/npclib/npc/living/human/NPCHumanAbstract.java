package org.inventivetalent.npclib.npc.living.human;

import org.bukkit.entity.HumanEntity;
import org.inventivetalent.npclib.entity.living.human.NPCEntityHuman;
import org.inventivetalent.npclib.npc.living.NPCLivingAbstract;

public abstract class NPCHumanAbstract<N extends NPCEntityHuman, B extends HumanEntity> extends NPCLivingAbstract<N, B> {
	public NPCHumanAbstract(N npcEntity) {
		super(npcEntity);
	}
}
