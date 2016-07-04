package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.PolarBear;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.EntityPolarBear;

@NPC(id = 102,
	 type = EntityType.POLAR_BEAR,
	 bukkit = PolarBear.class,
	 nms = "EntityPolarBear",
	 entity = EntityPolarBear.class)
public class NPCPolarBear extends NPCAnimalAbstract<EntityPolarBear, PolarBear> {
	public NPCPolarBear(EntityPolarBear npcEntity) {
		super(npcEntity);
	}
}
