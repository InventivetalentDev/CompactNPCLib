package org.inventivetalent.npclib.npc.living.insentient.water;

import org.bukkit.entity.WaterMob;
import org.inventivetalent.npclib.entity.living.insentient.water.NPCEntityWaterAnimal;
import org.inventivetalent.npclib.npc.living.insentient.NPCInsentientAbstract;

public abstract class NPCWaterAnimalAbstract<N extends NPCEntityWaterAnimal, B extends WaterMob> extends NPCInsentientAbstract<N, B> {
	protected NPCWaterAnimalAbstract(N npcEntity) {
		super(npcEntity);
	}
}
