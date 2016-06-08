package org.inventivetalent.npclib.npc.living.insentient.creature;

import org.bukkit.entity.Creature;
import org.inventivetalent.npclib.entity.living.insentient.creature.NPCEntityCreature;
import org.inventivetalent.npclib.npc.living.insentient.NPCInsentientAbstract;

public abstract class NPCCreatureAbstract<T extends NPCEntityCreature, B extends Creature> extends NPCInsentientAbstract<T, B> {
	protected NPCCreatureAbstract(T npcEntity) {
		super(npcEntity);
	}
}
