package org.inventivetalent.npclib.npc.living.insentient.creature.golem;

import org.bukkit.entity.Golem;
import org.inventivetalent.npclib.entity.living.insentient.creature.golem.NPCEntityGolem;
import org.inventivetalent.npclib.npc.living.insentient.creature.NPCCreatureAbstract;

public abstract class NPCGolemAbstract<N extends NPCEntityGolem, B extends Golem> extends NPCCreatureAbstract<N, B> {
	protected NPCGolemAbstract(N npcEntity) {
		super(npcEntity);
	}
}
