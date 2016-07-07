package org.inventivetalent.npclib.nbt;

import org.inventivetalent.nbt.NBTTag;

public abstract class NBTMember {

	protected final String[] key;
	protected final int      type;
	protected final Object   obj;
	protected final boolean  read;
	protected final boolean  write;

	public NBTMember(String[] key, int type, Object obj, boolean read, boolean write) {
		this.key = key;
		this.type = type;
		this.obj = obj;
		this.read = read;
		this.write = write;
	}

	public abstract void read(NBTTag tag);

	public abstract NBTTag write(NBTTag tag);

	public String[] getKey() {
		return key;
	}

	public int getType() {
		return type;
	}

	public boolean isRead() {
		return read;
	}

	public boolean isWrite() {
		return write;
	}
}
