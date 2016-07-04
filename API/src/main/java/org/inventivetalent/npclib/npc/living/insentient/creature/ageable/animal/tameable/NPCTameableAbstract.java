package org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal.tameable;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Tameable;
import org.inventivetalent.npclib.entity.living.insentient.creature.ageable.animal.tameable.NPCEntityTameable;
import org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal.NPCAnimalAbstract;

// *sigh* why doesn't Tameable extend Animals?!
public abstract class NPCTameableAbstract<N extends NPCEntityTameable, B extends Tameable & Animals> extends NPCAnimalAbstract<N, B> {
	public NPCTameableAbstract(N npcEntity) {
		super(npcEntity);
	}
}
