package org.inventivetalent.npclib.npc;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.EntityExperienceOrb;

@NPC(id = 2,
	 type = EntityType.EXPERIENCE_ORB,
	 bukkit = ExperienceOrb.class,
	 nms = "EntityExperienceOrb",
	 entity = EntityExperienceOrb.class)
public class NPCExperienceOrb extends NPCAbstract<EntityExperienceOrb, ExperienceOrb> {
	protected NPCExperienceOrb(EntityExperienceOrb npcEntity) {
		super(npcEntity);
	}
}
