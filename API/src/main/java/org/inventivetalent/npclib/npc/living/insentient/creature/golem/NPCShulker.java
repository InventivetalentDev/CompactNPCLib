package org.inventivetalent.npclib.npc.living.insentient.creature.golem;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Shulker;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.golem.EntityShulker;

@NPC(id = 69,
	 type = EntityType.SHULKER,
	 bukkit = Shulker.class,
	 nms = "EntityShulker",
	 entity = EntityShulker.class)
public class NPCShulker extends NPCGolemAbstract<EntityShulker, Shulker> {
	protected NPCShulker(EntityShulker npcEntity) {
		super(npcEntity);
	}
}
