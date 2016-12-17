package org.inventivetalent.npclib.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.inventivetalent.npclib.npc.NPCAbstract;

/**
 * Event called when a NPC dies.
 *
 * Note: This event is called twice for living entities. Once for the LivingEntity with a damage source, once for the base entity without a source.
 */
public class NPCDeathEvent extends NPCEvent implements Cancellable {

	private static HandlerList handlerList = new HandlerList();
	private boolean                       cancelled;
	private String                        damageSourceName;
	private EntityDamageEvent.DamageCause damageCause;

	public NPCDeathEvent(NPCAbstract<?, ?> npc, String damageSourceName, EntityDamageEvent.DamageCause damageCause) {
		super(npc);
		this.damageSourceName = damageSourceName;
		this.damageCause = damageCause;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	/**
	 * Whether this death event has a source. Always <code>false</code> for non-living entities
	 *
	 * @return <code>true</code> if the death has a source
	 */
	public boolean hasSource() {
		return damageSourceName != null;
	}

	/**
	 * Get the damage source name
	 *
	 * @return the source name
	 */
	public String getDamageSourceName() {
		return damageSourceName;
	}

	/**
	 * Get the damage cause
	 *
	 * @return the {@link org.bukkit.event.entity.EntityDamageEvent.DamageCause}
	 */
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

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}
