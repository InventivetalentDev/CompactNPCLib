package org.inventivetalent.npclib.event;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.inventivetalent.npclib.npc.NPCAbstract;

public class NPCSpawnEvent extends NPCEvent {

	private CreatureSpawnEvent.SpawnReason spawnReason;

	public NPCSpawnEvent(NPCAbstract npc, CreatureSpawnEvent.SpawnReason spawnReason) {
		super(npc);
		this.spawnReason = spawnReason;
	}

	public Location getLocation() {
		return getNpc().getBukkitEntity().getLocation();
	}

	public CreatureSpawnEvent.SpawnReason getSpawnReason() {
		return spawnReason;
	}

	public void setSpawnReason(CreatureSpawnEvent.SpawnReason spawnReason) {
		Preconditions.checkNotNull(spawnReason);
		this.spawnReason = spawnReason;
	}

	private static HandlerList handlerList = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}
}
