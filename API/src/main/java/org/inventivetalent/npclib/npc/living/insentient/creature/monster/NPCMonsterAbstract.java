package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.Monster;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.NPCEntityMonster;
import org.inventivetalent.npclib.npc.living.insentient.NPCInsentientAbstract;

public abstract class NPCMonsterAbstract<N extends NPCEntityMonster, B extends Monster> extends NPCInsentientAbstract<N, B> {
	public NPCMonsterAbstract(N npcEntity) {
		super(npcEntity);
	}
}
