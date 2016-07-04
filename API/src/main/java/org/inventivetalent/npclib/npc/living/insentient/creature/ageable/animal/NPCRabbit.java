package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.EntityRabbit;

@NPC(id = 101,
	 type = EntityType.RABBIT,
	 bukkit = Rabbit.class,
	 nms = "EntityRabbit",
	 entity = EntityRabbit.class)
public class NPCRabbit extends NPCAnimalAbstract<EntityRabbit, Rabbit> {
	protected NPCRabbit(EntityRabbit npcEntity) {
		super(npcEntity);
	}
}
