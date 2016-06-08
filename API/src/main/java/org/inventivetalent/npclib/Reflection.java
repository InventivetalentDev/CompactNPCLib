package org.inventivetalent.npclib;

import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.ClassResolver;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;
import org.inventivetalent.reflection.resolver.minecraft.OBCClassResolver;
import org.inventivetalent.reflection.resolver.wrapper.ClassWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Reflection {

	public static final boolean is1_9 = Minecraft.VERSION.newerThan(Minecraft.Version.v1_9_R1);
	public static final boolean is1_8 = !is1_9 && Minecraft.VERSION.newerThan(Minecraft.Version.v1_8_R1);
	public static final boolean is1_7 = !is1_8 && Minecraft.VERSION.newerThan(Minecraft.Version.v1_7_R1);

	public static final ClassResolver    classResolver    = new ClassResolver();
	public static final NMSClassResolver nmsClassResolver = new NMSClassResolver();
	public static final OBCClassResolver obcClassResolver = new OBCClassResolver();

	public static <T> T newInstance(String packageName, String className, Class<? extends T> type, Object... args) {
		ClassWrapper classWrapper = getVersionedClass(packageName, className);
		Constructor constructor = new ConstructorResolver(classWrapper.getClazz()).resolveFirstConstructorSilent();
		try {
			//noinspection unchecked
			return (T) constructor.newInstance(args);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the matching class for the current Minecraft version
	 *
	 * @param packageName package
	 * @param className   class
	 * @return the matching class
	 */
	public static ClassWrapper getVersionedClass(String packageName, String className) {
		return classResolver.resolveWrapper(packageName + "." + className + "_" + Minecraft.VERSION.name());
	}

	public static <T> T newLazyInstance(String packageName, String className, Class<? extends T> type, Object args) {
		ClassWrapper classWrapper = getLazyVersionedClass(packageName, className);
		Constructor constructor = new ConstructorResolver(classWrapper.getClazz()).resolveFirstConstructorSilent();
		try {
			//noinspection unchecked
			return (T) constructor.newInstance(args);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get the lazy (without release number) matching class for the current Minecraft version
	 *
	 * @param packageName package
	 * @param className   class
	 * @return the matching class
	 */
	public static ClassWrapper getLazyVersionedClass(String packageName, String className) {
		String version = Minecraft.VERSION.name();
		String lazyVersion = version.substring(0, version.lastIndexOf("_R"));// Remove _R?
		return classResolver.resolveWrapper(packageName + "." + className + "_" + lazyVersion);
	}

	public static boolean doesClassExist(String name) {
		try {
			return Class.forName(name) != null;
		} catch (ClassNotFoundException e) {
		}
		return false;
	}

	public static String getMethodSignature(Method method) {
		StringBuilder stringBuilder = new StringBuilder(method.getName());
		stringBuilder.append("(");

		boolean first = true;
		for (Class clazz : method.getParameterTypes()) {
			if (!first) { stringBuilder.append(","); }
			stringBuilder.append(clazz.getSimpleName());
			first = false;
		}
		return stringBuilder.append(")").toString();
	}

}
