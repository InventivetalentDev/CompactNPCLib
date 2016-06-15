package org.inventivetalent.npclib.event;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.inventivetalent.npclib.NPCType;
import org.inventivetalent.npclib.npc.NPCAbstract;

public abstract class NPCEvent extends Event {

	private final NPCAbstract npc;

	public NPCEvent(NPCAbstract npc) {
		this.npc = npc;
	}

	public NPCAbstract getNpc() {
		return npc;
	}

	public NPCType getNpcType() {
		return npc.getNpcType();
	}

	public EntityType getEntityType() {
		return npc.getEntityType();
	}
}
