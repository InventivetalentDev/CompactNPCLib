package org.inventivetalent.npclib.event.nbt;

import org.inventivetalent.nbt.NBTTag;
import org.inventivetalent.npclib.event.NPCEvent;
import org.inventivetalent.npclib.npc.NPCAbstract;

public abstract class NPCNBTEvent extends NPCEvent {

	private final Object nbtTagHandle;
	private final NBTTag nbtTag;

	public NPCNBTEvent(NPCAbstract npc, Object nbtTagHandle, NBTTag nbtTag) {
		super(npc);
		this.nbtTagHandle = nbtTagHandle;
		this.nbtTag = nbtTag;
	}

	public Object getNbtTagHandle() {
		return nbtTagHandle;
	}

	public NBTTag getNbtTag() {
		return nbtTag;
	}
}
