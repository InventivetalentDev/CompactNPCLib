package org.inventivetalent.npclib.npc.living;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.EntityArmorStand;
import org.inventivetalent.npclib.entity.living.NPCEntityLiving;

@NPC(id = 30,
	 type = EntityType.ARMOR_STAND,
	 bukkit = ArmorStand.class,
	 nms = "EntityArmorStand",
	 entity = EntityArmorStand.class)
public class NPCArmorStand<N extends NPCEntityLiving, B extends LivingEntity> extends NPCLivingAbstract<N, B> {
	protected NPCArmorStand(N npcEntity) {
		super(npcEntity);
	}
}
