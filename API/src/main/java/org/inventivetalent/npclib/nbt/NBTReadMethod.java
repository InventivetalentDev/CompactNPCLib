package org.inventivetalent.npclib.nbt;

import org.inventivetalent.nbt.CompoundTag;
import org.inventivetalent.nbt.NBTTag;
import org.inventivetalent.nbt.TagID;

import java.lang.reflect.Method;

public class NBTReadMethod extends NBTMember {

	private final Method         method;
	private final NBTParameter[] parameters;

	public NBTReadMethod(String[] key, int type, boolean read, Object obj, Method method, NBTParameter[] parameters) {
		super(key, type, read, false, obj);
		this.method = method;
		this.parameters = parameters;
	}

	@Override
	public void read(NBTTag tag) {
		Object[] args = new Object[parameters.length];
		for (int i = 0; i < args.length; i++) {
			NBTTag paramTag;
			if (tag.getTypeId() == TagID.TAG_COMPOUND) {
				paramTag = AnnotatedNBTHandler.digTag((CompoundTag) tag, parameters[i].key, 0);
			} else {
				//TODO: check if the parameter length == 1
				paramTag = tag;
			}
			args[i] = paramTag == null ? null : fromNbtValue(paramTag.getValue(), parameters[i].parameter.getType());
		}
		try {
			method.invoke(this.obj, args);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public NBTTag write(NBTTag tag) {
		throw new UnsupportedOperationException();
	}
}
