package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.EntityPig;

@NPC(id = 90,
	 type = EntityType.PIG,
	 bukkit = Pig.class,
	 nms = "EntityPig",
	 entity = EntityPig.class)
public class NPCPig extends NPCAnimalAbstract<EntityPig, Pig> {
	protected NPCPig(EntityPig npcEntity) {
		super(npcEntity);
	}
}
