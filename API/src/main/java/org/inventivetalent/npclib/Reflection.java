/*
 * Copyright 2016 inventivetalent.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
 */

package org.inventivetalent.npclib;

import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.ClassResolver;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;
import org.inventivetalent.reflection.resolver.minecraft.OBCClassResolver;
import org.inventivetalent.reflection.resolver.wrapper.ClassWrapper;

import java.lang.reflect.Constructor;

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

}
