package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.EntityHorse;

@NPC(id = 100,
	 type = EntityType.HORSE,
	 bukkit = Horse.class,
	 nms = "EntityHorse",
	 entity = EntityHorse.class)
public class NPCHorse extends NPCAnimalAbstract<EntityHorse, Horse> {
	public NPCHorse(EntityHorse npcEntity) {
		super(npcEntity);
	}
}
