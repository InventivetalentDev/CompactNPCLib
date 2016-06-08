package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntityWither;

@NPC(id = 64,
	 type = EntityType.WITHER,
	 bukkit = Wither.class,
	 nms = "EntityWither",
	 entity = EntityWither.class)
public class NPCWither extends NPCMonsterAbstract<EntityWither, Wither> {
	protected NPCWither(EntityWither npcEntity) {
		super(npcEntity);
	}
}
