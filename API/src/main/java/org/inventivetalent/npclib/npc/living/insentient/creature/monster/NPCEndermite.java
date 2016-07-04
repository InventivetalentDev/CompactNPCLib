package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.Endermite;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntityEndermite;

@NPC(id = 67,
	 type = EntityType.ENDERMITE,
	 bukkit = Endermite.class,
	 nms = "EntityEndermite",
	 entity = EntityEndermite.class)
public class NPCEndermite extends NPCMonsterAbstract<EntityEndermite, Endermite> {
	public NPCEndermite(EntityEndermite npcEntity) {
		super(npcEntity);
	}
}
