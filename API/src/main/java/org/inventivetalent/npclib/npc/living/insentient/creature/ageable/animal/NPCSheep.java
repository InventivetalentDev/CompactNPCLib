package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.EntitySheep;

@NPC(id = 91,
	 type = EntityType.SHEEP,
	 bukkit = Sheep.class,
	 nms = "EntitySheep",
	 entity = EntitySheep.class)
public class NPCSheep extends NPCAnimalAbstract<EntitySheep, Sheep> {
	protected NPCSheep(EntitySheep npcEntity) {
		super(npcEntity);
	}
}
