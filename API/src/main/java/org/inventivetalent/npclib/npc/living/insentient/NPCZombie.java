package org.inventivetalent.npclib.npc.living.insentient;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.EntityZombie;
import org.inventivetalent.npclib.npc.living.insentient.NPCInsentientAbstract;

@NPC(id = 54,
	 type = EntityType.ZOMBIE,
	 bukkit = Zombie.class,
	 nms = "EntityZombie",
	 entity = EntityZombie.class)
public class NPCZombie extends NPCInsentientAbstract<EntityZombie, Zombie> {

	protected NPCZombie(EntityZombie npcEntity) {
		super(npcEntity);
	}

}
