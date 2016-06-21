/*
 * Copyright 2015-2016 inventivetalent. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
 */

package org.inventivetalent.npclib.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.inventivetalent.npclib.npc.NPCAbstract;

import javax.annotation.Nullable;

/**
 * Called if an NPC is damaged
 */
public class NPCDamageEvent extends NPCEvent implements Cancellable {

	private boolean cancelled;

	private String                        damageSourceName;
	private EntityDamageEvent.DamageCause damageCause;
	private float                         amount;
	private Entity                        damager;

	public NPCDamageEvent(NPCAbstract npc, String damageSourceName, EntityDamageEvent.DamageCause damageCause, float amount, Entity damager) {
		super(npc);

		this.damageSourceName = damageSourceName;
		this.damageCause = damageCause;
		this.amount = amount;
		this.damager = damager;
	}

	/**
	 * Get the NMS damage source name
	 *
	 * @return the source name
	 */
	public String getDamageSourceName() {
		return damageSourceName;
	}

	/**
	 * Get the damage cause
	 *
	 * @return the event's {@link org.bukkit.event.entity.EntityDamageEvent.DamageCause}
	 */
	public EntityDamageEvent.DamageCause getDamageCause() {
		return damageCause;
	}

	/**
	 * Get the amount
	 *
	 * @return the damage amount
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * Set the amount
	 *
	 * @param amount the new damage amount
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * Get the damager
	 *
	 * @return the entity that damaged the NPC, or <code>null</code>
	 */
	@Nullable
	public Entity getDamager() {
		return damager;
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
