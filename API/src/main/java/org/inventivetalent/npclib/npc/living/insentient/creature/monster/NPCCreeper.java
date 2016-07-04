package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntityCreeper;

@NPC(id = 50,
	 type = EntityType.CREEPER,
	 bukkit = Creeper.class,
	 nms = "EntityCreeper",
	 entity = EntityCreeper.class)
public class NPCCreeper extends NPCMonsterAbstract<EntityCreeper, Creeper> {
	public NPCCreeper(EntityCreeper npcEntity) {
		super(npcEntity);
	}
}
