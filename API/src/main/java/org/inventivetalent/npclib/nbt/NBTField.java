package org.inventivetalent.npclib.nbt;

import org.inventivetalent.nbt.ByteTag;
import org.inventivetalent.nbt.NBTTag;

import java.lang.reflect.Field;

public class NBTField extends NBTMember {

	private final Field field;

	public NBTField(String[] key, int type, Object obj, boolean read, boolean write, Field field) {
		super(key, type, obj, read, write);
		this.field = field;
	}

	@Override
	public void read(NBTTag tag) {
		try {
			if (boolean.class.isAssignableFrom(field.getType())) {
				field.setBoolean(this.obj, ((ByteTag) tag).getValue() == 1);
			} else {
				field.set(this.obj, tag.getValue());
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public NBTTag write(NBTTag tag) {
		try {
			Object value = field.get(this.obj);
			if (boolean.class.isAssignableFrom(field.getType())) {
				value = (byte) ((boolean) value ? 1 : 0);
			}
			tag.setValue(value);
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
