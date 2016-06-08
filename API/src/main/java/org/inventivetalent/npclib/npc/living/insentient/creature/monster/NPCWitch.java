package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Witch;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntityWitch;

@NPC(id = 66,
	 type = EntityType.WITCH,
	 bukkit = Witch.class,
	 nms = "EntityWitch",
	 entity = EntityWitch.class)
public class NPCWitch extends NPCMonsterAbstract<EntityWitch, Witch> {
	protected NPCWitch(EntityWitch npcEntity) {
		super(npcEntity);
	}
}
