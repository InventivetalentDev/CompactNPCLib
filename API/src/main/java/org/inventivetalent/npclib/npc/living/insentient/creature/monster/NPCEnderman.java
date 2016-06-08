package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntityEnderman;

@NPC(id = 58,
	 type = EntityType.ENDERMAN,
	 bukkit = Enderman.class,
	 nms = "EntityEnderman",
	 entity = EntityEnderman.class)
public class NPCEnderman extends NPCMonsterAbstract<EntityEnderman, Enderman> {
	protected NPCEnderman(EntityEnderman npcEntity) {
		super(npcEntity);
	}
}
