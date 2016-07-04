package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal;

import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.EntityCow;

@NPC(id = 92,
	 type = EntityType.COW,
	 bukkit = Cow.class,
	 nms = "EntityCow",
	 entity = EntityCow.class)
public class NPCCow extends NPCAnimalAbstract<EntityCow, Cow> {
	public NPCCow(EntityCow npcEntity) {
		super(npcEntity);
	}
}
