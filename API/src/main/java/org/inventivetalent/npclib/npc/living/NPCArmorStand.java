package org.inventivetalent.npclib.npc.living;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.EntityArmorStand;

@NPC(id = 30,
	 type = EntityType.ARMOR_STAND,
	 bukkit = ArmorStand.class,
	 nms = "EntityArmorStand",
	 entity = EntityArmorStand.class)
public class NPCArmorStand extends NPCLivingAbstract<EntityArmorStand, ArmorStand> {
	protected NPCArmorStand(EntityArmorStand npcEntity) {
		super(npcEntity);
	}
}
