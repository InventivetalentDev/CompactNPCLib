package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal.tameable;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.tameable.EntityOcelot;

@NPC(id = 98,
	 type = EntityType.OCELOT,
	 bukkit = Ocelot.class,
	 nms = "EntityOcelot",
	 entity = EntityOcelot.class)
public class NPCOcelot extends NPCTameableAbstract<EntityOcelot, Ocelot> {
	protected NPCOcelot(EntityOcelot npcEntity) {
		super(npcEntity);
	}
}
