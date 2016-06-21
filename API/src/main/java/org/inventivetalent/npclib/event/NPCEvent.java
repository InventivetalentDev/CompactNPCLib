package org.inventivetalent.npclib.event;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.inventivetalent.npclib.NPCType;
import org.inventivetalent.npclib.npc.NPCAbstract;

/**
 * Abstract base class for NPC events
 */
public abstract class NPCEvent extends Event {

	private final NPCAbstract npc;

	public NPCEvent(NPCAbstract npc) {
		this.npc = npc;
	}

	/**
	 * Get the involved NPC
	 *
	 * @return the NPC
	 */
	public NPCAbstract getNpc() {
		return npc;
	}

	/**
	 * Get the NPC's type
	 *
	 * @return the {@link NPCType}
	 */
	public NPCType getNpcType() {
		return npc.getNpcType();
	}

	/**
	 * Get the NPC's entity type
	 *
	 * @return the {@link EntityType}
	 */
	public EntityType getEntityType() {
		return npc.getEntityType();
	}
}
