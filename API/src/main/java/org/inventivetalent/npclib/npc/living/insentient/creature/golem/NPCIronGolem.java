package org.inventivetalent.npclib.npc.living.insentient.creature.golem;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.golem.EntityIronGolem;

@NPC(id = 99,
	 type = EntityType.IRON_GOLEM,
	 bukkit = IronGolem.class,
	 nms = "EntityIronGolem",
	 entity = EntityIronGolem.class)
public class NPCIronGolem extends NPCGolemAbstract<EntityIronGolem, IronGolem> {
	protected NPCIronGolem(EntityIronGolem npcEntity) {
		super(npcEntity);
	}
}
