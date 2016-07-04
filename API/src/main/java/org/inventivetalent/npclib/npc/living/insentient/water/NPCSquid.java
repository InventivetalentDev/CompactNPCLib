package org.inventivetalent.npclib.npc.living.insentient.water;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.water.EntitySquid;

@NPC(id = 94,
	 type = EntityType.SQUID,
	 bukkit = Squid.class,
	 nms = "EntitySquid",
	 entity = EntitySquid.class)
public class NPCSquid extends NPCWaterAnimalAbstract<EntitySquid, Squid> {
	public NPCSquid(EntitySquid npcEntity) {
		super(npcEntity);
	}
}
