package org.inventivetalent.npclib.npc.living.insentient.flying;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.flying.EntityGhast;

@NPC(id = 56,
	 type = EntityType.GHAST,
	 bukkit = Ghast.class,
	 nms = "EntityGhast",
	 entity = EntityGhast.class)
public class NPCGhast extends NPCFlyingAbstract<EntityGhast, Ghast> {
	protected NPCGhast(EntityGhast npcEntity) {
		super(npcEntity);
	}
}
