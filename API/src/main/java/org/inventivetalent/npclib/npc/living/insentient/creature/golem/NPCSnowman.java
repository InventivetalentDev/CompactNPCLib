package org.inventivetalent.npclib.npc.living.insentient.creature.golem;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowman;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.golem.EntitySnowman;

@NPC(id = 97,
	 type = EntityType.SNOWMAN,
	 bukkit = Snowman.class,
	 nms = "EntitySnowman",
	 entity = EntitySnowman.class)
public class NPCSnowman extends NPCGolemAbstract<EntitySnowman, Snowman> {
	public NPCSnowman(EntitySnowman npcEntity) {
		super(npcEntity);
	}
}
