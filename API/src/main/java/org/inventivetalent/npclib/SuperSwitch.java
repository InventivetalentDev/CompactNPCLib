package org.inventivetalent.npclib;

import lombok.*;
import org.bukkit.event.Cancellable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SuperSwitch implements Cancellable {

	private State state = State.PASS;

	public static SuperSwitch newInstance() {
		return new SuperSwitch();
	}

	@Override
	public boolean isCancelled() {
		return this.state == State.CANCEL;
	}

	@Override
	public void setCancelled(boolean b) {
		this.state = State.CANCEL;
	}

	public boolean callSuper() {
		return this.state == State.PASS || this.state == State.REPLACE;
	}

	public boolean isReplace() {
		return this.state == State.REPLACE;
	}

	public enum State {
		/**
		 * Let the call pass and return the default value by calling the super method
		 */
		PASS,
		/**
		 * Cancel the call entirely and don't call super
		 */
		CANCEL,
		/**
		 * Call super, but replace the returned value of the method
		 */
		REPLACE;
	}
}
