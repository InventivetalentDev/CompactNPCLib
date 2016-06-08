package org.inventivetalent.npclib.npc.living.insentient.flying;

import org.bukkit.entity.Flying;
import org.inventivetalent.npclib.entity.living.insentient.flying.NPCEntityFlying;
import org.inventivetalent.npclib.npc.living.insentient.NPCInsentientAbstract;

public abstract class NPCFlyingAbstract<N extends NPCEntityFlying, B extends Flying> extends NPCInsentientAbstract<N, B> {
	protected NPCFlyingAbstract(N npcEntity) {
		super(npcEntity);
	}
}
