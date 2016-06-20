package org.inventivetalent.npclib;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ObjectContainer<T> {

	public T value;

	@Override
	public String toString() {
		if (value == null) { return "ObjectContainer[null]"; }
		return value.toString();
	}

	public static ObjectContainer[] fromObjects(Object[] objects) {
		ObjectContainer[] containers = new ObjectContainer[objects.length];
		for (int i = 0; i < objects.length; i++)
			containers[i] = new ObjectContainer<>(objects[i]);
		return containers;
	}

	public static Object[] toObjects(ObjectContainer[] containers) {
		Object[] objects = new Object[containers.length];
		for (int i = 0; i < containers.length; i++)
			objects[i] = containers[i].value;
		return objects;
	}

}
