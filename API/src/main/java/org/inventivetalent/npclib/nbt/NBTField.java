package org.inventivetalent.npclib.nbt;

import org.inventivetalent.nbt.NBTTag;

import java.lang.reflect.Field;

public class NBTField extends NBTMember {

	private final Field field;

	public NBTField(String[] key, int type, boolean read, boolean write, Object obj, Field field) {
		super(key, type, read, write, obj);
		this.field = field;
	}

	@Override
	public void read(NBTTag tag) {
		try {
			field.set(this.obj, fromNbtValue(tag.getValue(), field.getType()));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public NBTTag write(NBTTag tag) {
		try {
			Object value = toNbtValue(field.get(this.obj), field.getType());
			if (value != null) {
				tag.setValue(value);
			}
			return tag;
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public Object get() {
		try {
			return field.get(this.obj);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public NBTTag getNBT() {
		try {
			Object value = field.get(this.obj);
			NBTTag tag = NBTTag.createType(this.type);
			tag.setValue(value);
			return tag;
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public void set(Object object) {
		try {
			field.set(this.obj, object);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public void set(NBTTag tag) {
		set(tag.getValue());
	}

	public Field getField() {
		return field;
	}
}
