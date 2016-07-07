package org.inventivetalent.npclib.nbt;

import org.inventivetalent.nbt.CompoundTag;
import org.inventivetalent.nbt.NBTTag;
import org.inventivetalent.nbt.TagID;
import org.inventivetalent.npclib.NPCLib;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AnnotatedNBTHandler {

	private final Object toHandle;
	private final Set<NBTMember> members = new HashSet<>();

	public AnnotatedNBTHandler(Object toHandle) {
		this(toHandle, true);
	}

	public AnnotatedNBTHandler(Object toHandle, boolean searchSuper) {
		this.toHandle = toHandle;
		if (searchSuper) {
			Class superClazz = toHandle.getClass();
			while (superClazz != null) {
				register(superClazz);
				superClazz = superClazz.getSuperclass();
			}
		} else {
			register(toHandle.getClass());
		}
	}

	void register(Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			NBT annotation = field.getAnnotation(NBT.class);
			if (annotation != null) {
				field.setAccessible(true);

				String[] key = annotation.value();
				if (key == null || key.length == 0) { key = new String[] { field.getName() }; }
				int type = annotation.type();
				boolean write = annotation.write();
				boolean read = annotation.read();

				NPCLib.debug("@NBT annotation on", field.getName(), "(", Arrays.toString(key), ") in", clazz.getName());
				members.add(new NBTField(key, type, this.toHandle, read, write, field));
			}
		}
	}

	NBTTag digTag(CompoundTag parent, String[] key, int index) {
		NBTTag tag = parent.get(key[index]);
		if (tag == null) { return null; }
		if (tag.getTypeId() != TagID.TAG_COMPOUND) {
			if (index != key.length - 1) {
				throw new IllegalStateException("Found non-compound tag before key end (index " + index + "/" + key.length + ")");
			}
			return tag;
		}
		return digTag((CompoundTag) tag, key, index + 1);
	}

	NBTTag buildTag(CompoundTag parent, int type, String[] key) {
		try {
			for (int i = 0; i < key.length - 1; i++) {
				parent = parent.getOrCreateCompound(key[i]);
			}
			NBTTag tag = NBTTag.createType(type, key[key.length - 1]);
			parent.set(key[key.length - 1], tag);
			return tag;
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public void onRead(CompoundTag compoundTag) {
		for (NBTMember member : members) {
			if (member.read) {
				member.read(digTag(compoundTag, member.key, 0));
			}
		}
	}

	public void onWrite(CompoundTag compoundTag) {
		for (NBTMember member : members) {
			if (member.write) {
				NBTTag tag = buildTag(compoundTag, member.type, member.key);
				member.write(tag);
			}
		}
	}

}
