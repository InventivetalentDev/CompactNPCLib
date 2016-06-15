package org.inventivetalent.npclib.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.inventivetalent.npclib.npc.NPCAbstract;

public class NPCDeathEvent extends NPCEvent implements Cancellable {

	private boolean                       cancelled;
	private String                        damageSourceName;
	private EntityDamageEvent.DamageCause damageCause;

	public NPCDeathEvent(NPCAbstract npc, String damageSourceName, EntityDamageEvent.DamageCause damageCause) {
		super(npc);
		this.damageSourceName = damageSourceName;
		this.damageCause = damageCause;
	}

	/**
	 * Whether this death event has a source. Always <code>false</code> for non-living entities
	 *
	 * @return <code>true</code> if the death has a source
	 */
	public boolean hasSource() {
		return damageSourceName != null;
	}

	public String getDamageSourceName() {
		return damageSourceName;
	}

	public EntityDamageEvent.DamageCause getDamageCause() {
		return damageCause;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean b) {
		cancelled = b;
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
