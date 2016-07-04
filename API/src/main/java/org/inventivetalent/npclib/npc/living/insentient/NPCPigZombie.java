package org.inventivetalent.npclib.npc.living.insentient;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.EntityPigZombie;
import org.inventivetalent.npclib.entity.living.insentient.EntityZombie;

@NPC(id = 57,
	 type = EntityType.PIG_ZOMBIE,
	 bukkit = PigZombie.class,
	 nms = "EntityPigZombie",
	 entity = EntityPigZombie.class)
public class NPCPigZombie extends NPCZombie {
	public NPCPigZombie(EntityZombie npcEntity) {
		super(npcEntity);
	}
}
