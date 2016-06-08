package org.inventivetalent.npclib.npc.living.insentient.creature.monster;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.monster.EntitySkeleton;

@NPC(id = 51,
	 type = EntityType.SKELETON,
	 bukkit = Skeleton.class,
	 nms = "EntitySkeleton",
	 entity = EntitySkeleton.class)
public class NPCSkeleton extends NPCMonsterAbstract<EntitySkeleton, Skeleton> {
	protected NPCSkeleton(EntitySkeleton npcEntity) {
		super(npcEntity);
	}
}
