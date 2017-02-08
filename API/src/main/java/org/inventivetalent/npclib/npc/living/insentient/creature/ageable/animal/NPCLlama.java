package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Llama;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.EntityLlama;

@NPC(id = 103,
	 type = EntityType.LLAMA,
	 bukkit = Llama.class,
	 nms = "EntityLlama",
	 entity = EntityLlama.class)
public class NPCLlama extends NPCAnimalAbstract<EntityLlama, Llama> {
	public NPCLlama(EntityLlama npcEntity) {
		super(npcEntity);
	}
}
