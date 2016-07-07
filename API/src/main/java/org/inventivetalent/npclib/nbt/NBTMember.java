package org.inventivetalent.npclib.nbt;

import org.inventivetalent.nbt.NBTTag;

public abstract class NBTMember extends NBTInfo {

	protected final Object obj;

	public NBTMember(String[] key, int type, boolean read, boolean write, Object obj) {
		super(key, type, read, write);
		this.obj = obj;
	}

	public abstract void read(NBTTag tag);

	public abstract NBTTag write(NBTTag tag);

}
