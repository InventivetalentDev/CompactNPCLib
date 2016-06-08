package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal;

import org.bukkit.entity.Animals;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.NPCEntityAnimal;
import org.inventivetalent.npclib.npc.living.insentient.creature.ageable.NPCAgeableAbstract;

public class NPCAnimalAbstract<N extends NPCEntityAnimal, B extends Animals> extends NPCAgeableAbstract<N, B> {
	protected NPCAnimalAbstract(N npcEntity) {
		super(npcEntity);
	}
}
