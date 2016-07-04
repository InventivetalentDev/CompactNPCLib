package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.EntityChicken;

@NPC(id = 93,
	 type = EntityType.CHICKEN,
	 bukkit = Chicken.class,
	 nms = "EntityChicken",
	 entity = EntityChicken.class)
public class NPCChicken extends NPCAnimalAbstract<EntityChicken, Chicken> {
	public NPCChicken(EntityChicken npcEntity) {
		super(npcEntity);
	}
}
