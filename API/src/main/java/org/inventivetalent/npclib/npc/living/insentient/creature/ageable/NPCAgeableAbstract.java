package org.inventivetalent.npclib.npc.living.insentient.creature.ageable;

import org.bukkit.entity.Ageable;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.NPCEntityAgeable;
import org.inventivetalent.npclib.npc.living.insentient.creature.NPCCreatureAbstract;

public abstract class NPCAgeableAbstract<N extends NPCEntityAgeable, B extends Ageable> extends NPCCreatureAbstract<N, B> {
	public NPCAgeableAbstract(N npcEntity) {
		super(npcEntity);
	}
}
