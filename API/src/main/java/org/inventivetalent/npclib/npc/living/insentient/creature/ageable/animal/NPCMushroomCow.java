package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.EntityCow;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.EntityMushroomCow;

@NPC(id = 96,
	 type = EntityType.MUSHROOM_COW,
	 bukkit = MushroomCow.class,
	 nms = "EntityMushroomCow",
	 entity = EntityMushroomCow.class)
public class NPCMushroomCow extends NPCCow {
	public NPCMushroomCow(EntityCow npcEntity) {
		super(npcEntity);
	}
}
