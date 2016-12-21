package org.inventivetalent.npclib.event;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.inventivetalent.npclib.npc.NPCAbstract;

import javax.annotation.Nonnull;

/**
 * Event called when a new NPC is spawned
 */
public class NPCSpawnEvent extends NPCEvent {

	private static HandlerList handlerList = new HandlerList();
	private CreatureSpawnEvent.SpawnReason spawnReason;

	public NPCSpawnEvent(NPCAbstract<?, ?> npc, CreatureSpawnEvent.SpawnReason spawnReason) {
		super(npc);
		this.spawnReason = spawnReason;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	/**
	 * Get the location where the NPC was spawned
	 *
	 * @return the spawn {@link Location}
	 */
	public Location getLocation() {
		return getNpc().getBukkitEntity().getLocation();
	}

	/**
	 * Get the spawn reason
	 *
	 * @return the {@link org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason}
	 */
	public CreatureSpawnEvent.SpawnReason getSpawnReason() {
		return spawnReason;
	}

	/**
	 * Set the spawn reason
	 *
	 * @param spawnReason the new spawn reason
	 */
	public void setSpawnReason(@Nonnull CreatureSpawnEvent.SpawnReason spawnReason) {
		Preconditions.checkNotNull(spawnReason);
		this.spawnReason = spawnReason;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
