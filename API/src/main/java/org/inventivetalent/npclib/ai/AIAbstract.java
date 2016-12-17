package org.inventivetalent.npclib.ai;

import org.inventivetalent.npclib.npc.NPCAbstract;

public class AIAbstract<N extends NPCAbstract<?, ?>> {

	private N npc;

	/**
	 * Called when the containing NPC is ticked
	 */
	public void tick() {
	}

	/**
	 * If this returns <code>true</code>, this AI will be removed from the NPC
	 *
	 * @return if <code>true</code>, the AI will be removed
	 */
	public boolean isFinished() {
		return false;
	}

	public final N getNpc() {
		if (this.npc == null) { throw new IllegalStateException("npc not set"); }
		return this.npc;
	}

	public final void setNpc(N npc) {
		if (this.npc != null) { throw new IllegalStateException("npc already set"); }
		this.npc = npc;
	}

}
