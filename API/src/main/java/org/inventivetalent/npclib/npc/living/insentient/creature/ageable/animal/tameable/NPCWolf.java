package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal.tameable;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.tameable.EntityWolf;

@NPC(id = 95,
	 type = EntityType.WOLF,
	 bukkit = Wolf.class,
	 nms = "EntityWolf",
	 entity = EntityWolf.class)
public class NPCWolf extends NPCTameableAbstract<EntityWolf, Wolf> {
	protected NPCWolf(EntityWolf npcEntity) {
		super(npcEntity);
	}
}
