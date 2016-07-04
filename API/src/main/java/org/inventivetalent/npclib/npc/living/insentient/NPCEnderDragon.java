package org.inventivetalent.npclib.npc.living.insentient;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.EntityEnderDragon;

@NPC(id = 63,
	 type = EntityType.ENDER_DRAGON,
	 bukkit = EnderDragon.class,
	 nms = "EntityEnderDragon",
	 entity = EntityEnderDragon.class)
public class NPCEnderDragon extends NPCInsentientAbstract<EntityEnderDragon, EnderDragon> {
	public NPCEnderDragon(EntityEnderDragon npcEntity) {
		super(npcEntity);
	}
}
