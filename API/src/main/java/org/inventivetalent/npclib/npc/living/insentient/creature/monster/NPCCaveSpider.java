package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntityCaveSpider;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntitySpider;

@NPC(id = 59,
	 type = EntityType.CAVE_SPIDER,
	 bukkit = CaveSpider.class,
	 nms = "EntityCaveSpider",
	 entity = EntityCaveSpider.class)
public class NPCCaveSpider extends NPCSpider {
	public NPCCaveSpider(EntitySpider npcEntity) {
		super(npcEntity);
	}
}
