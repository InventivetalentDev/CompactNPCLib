package org.inventivetalent.npclib.npc.living.insentient;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.EntitySlime;

@NPC(id = 55,
	 type = EntityType.SLIME,
	 bukkit = Slime.class,
	 nms = "EntitySlime",
	 entity = EntitySlime.class)
public class NPCSlime extends NPCInsentientAbstract<EntitySlime, Slime> {
	protected NPCSlime(EntitySlime npcEntity) {
		super(npcEntity);
	}
}
