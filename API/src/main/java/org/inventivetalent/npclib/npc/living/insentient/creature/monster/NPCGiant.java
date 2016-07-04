package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntityGiant;

@NPC(id = 53,
	 type = EntityType.GIANT,
	 bukkit = Giant.class,
	 nms = "EntityGiantZombie",
	 entity = EntityGiant.class)
public class NPCGiant extends NPCMonsterAbstract<EntityGiant, Giant> {
	public NPCGiant(EntityGiant npcEntity) {
		super(npcEntity);
	}
}
