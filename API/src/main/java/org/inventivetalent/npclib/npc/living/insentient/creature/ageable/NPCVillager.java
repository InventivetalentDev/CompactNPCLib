package org.inventivetalent.npclib.npc.living.insentient.creature.ageable;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.EntityVillager;

@NPC(id = 120,
	 type = EntityType.VILLAGER,
	 bukkit = Villager.class,
	 nms = "EntityVillager",
	 entity = EntityVillager.class)
public class NPCVillager extends NPCAgeableAbstract<EntityVillager, Villager> {
	public NPCVillager(EntityVillager npcEntity) {
		super(npcEntity);
	}
}
