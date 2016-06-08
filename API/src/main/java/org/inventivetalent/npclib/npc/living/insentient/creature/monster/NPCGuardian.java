package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntityGuardian;

@NPC(id = 68,
	 type = EntityType.GUARDIAN,
	 bukkit = Guardian.class,
	 nms = "EntityGuardian",
	 entity = EntityGuardian.class)
public class NPCGuardian extends NPCMonsterAbstract<EntityGuardian, Guardian> {
	protected NPCGuardian(EntityGuardian npcEntity) {
		super(npcEntity);
	}
}
