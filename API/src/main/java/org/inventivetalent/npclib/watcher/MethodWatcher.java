package org.inventivetalent.npclib.watcher;

import org.inventivetalent.npclib.ObjectContainer;
import org.inventivetalent.npclib.SuperSwitch;

public class MethodWatcher {

	/**
	 * Called for <code>void</code> methods only. If it returns <code>true</code>, the super method will be called
	 *
	 * @param methodSignature signature of the called method
	 * @param args            method arguments
	 * @return whether the super method will be called
	 */
	public boolean methodCalled(Object thiz, String methodSignature, ObjectContainer[] args) {
		return true;
	}

	/**
	 * Called for <code>non-void</code> methods.
	 *
	 * @param methodSignature signature of the called method
	 * @param superValue      original value returned by the superclass
	 * @param args            method arguments
	 * @return value the method returns
	 */
	public Object methodCalled(Object thiz, String methodSignature, SuperSwitch superSwitch, ObjectContainer[] args) {
		return null;
	}

}
