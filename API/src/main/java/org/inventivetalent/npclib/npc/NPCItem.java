package org.inventivetalent.npclib.npc;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.EntityItem;

@NPC(id = 1,
	 type = EntityType.DROPPED_ITEM,
	 bukkit = Item.class,
	 nms = "EntityItem",
	 entity = EntityItem.class)
public class NPCItem extends NPCAbstract<EntityItem, Item> {
	protected NPCItem(EntityItem npcEntity) {
		super(npcEntity);
	}
}
