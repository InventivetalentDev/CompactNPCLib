package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Husk;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.EntityZombie;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntityHusk;
import org.inventivetalent.npclib.npc.living.insentient.NPCZombie;

@NPC(id = 23,
	 type = EntityType.HUSK,
	 bukkit = Husk.class,
	 nms = "EntityZombieHusk",
	 entity = EntityHusk.class)
public class NPCHusk extends NPCZombie {
	public NPCHusk(EntityZombie npcEntity) {
		super(npcEntity);
	}
}
