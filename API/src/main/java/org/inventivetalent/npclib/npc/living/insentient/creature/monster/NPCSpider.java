package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntitySpider;

@NPC(id = 52,
	 type = EntityType.SPIDER,
	 bukkit = Spider.class,
	 nms = "EntitySpider",
	 entity = EntitySpider.class)
public class NPCSpider extends NPCMonsterAbstract<EntitySpider, Spider> {
	public NPCSpider(EntitySpider npcEntity) {
		super(npcEntity);
	}
}
