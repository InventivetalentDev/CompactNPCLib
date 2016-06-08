package org.inventivetalent.npclib;

public class ObjectConverter {

	public static Object toObject(Object o) {
		return o;
	}

	public static Object toObject(byte b) {
		return Byte.valueOf(b);
	}

	public static Object toObject(short s) {
		return Short.valueOf(s);
	}

	public static Object toObject(int i) {
		return Integer.valueOf(i);
	}

	public static Object toObject(long l) {
		return Long.valueOf(l);
	}

	public static Object toObject(float f) {
		return Float.valueOf(f);
	}

	public static Object toObject(double d) {
		return Double.valueOf(d);
	}

	public static Object toObject(boolean b) {
		return Boolean.valueOf(b);
	}

	public static Object toObject(char c) {
		return Character.valueOf(c);
	}

}
