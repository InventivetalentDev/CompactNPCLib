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

package org.inventivetalent.npclib.watcher;

import lombok.AllArgsConstructor;
import org.inventivetalent.npclib.Reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotatedMethodWatcher extends MethodWatcher {

	Object toWatch;
	Map<String, WatchedMethod> watchedMethods = new ConcurrentHashMap<>();

	public AnnotatedMethodWatcher(Object toWatch) {
		this(toWatch, true);
	}

	public AnnotatedMethodWatcher(Object toWatch, boolean searchSuper) {
		this.toWatch = toWatch;
		if (searchSuper) {
			Class superClazz = toWatch.getClass();
			while (superClazz != null) {
				register(superClazz);
				superClazz = superClazz.getSuperclass();
			}
		} else {
			register(toWatch.getClass());
		}
	}

	void register(Class<?> clazz) {
		for (Method method : clazz.getMethods()) {
			Watch annotation = method.getAnnotation(Watch.class);
			if (annotation != null) {
				String[] signatures = annotation.value();
				if (signatures.length == 0) {signatures = new String[] { Reflection.getMethodSignature(method) }; }
				boolean passThrough = true;
				if (method.getReturnType().equals(Void.TYPE)) { passThrough = annotation.passThrough(); }
				//				boolean ignoreThiz = annotation.ignoreThiz();

				for (String signature : signatures) {
					System.out.println("Watching method " + signature);
					watchedMethods.put(signature, new WatchedMethod(signature, passThrough, /*ignoreThiz,*/method.getReturnType().equals(Void.TYPE), method));
				}
			}
		}
	}

	@Override
	public boolean methodCalled(Object thiz, String methodSignature, Object[] args) {
		WatchedMethod watchedMethod = watchedMethods.get(methodSignature);
		if (watchedMethod == null) {
			return super.methodCalled(thiz, methodSignature, args);
		}
		try {
			Object returned = watchedMethod.method.invoke(toWatch, args);
			System.out.println("returned: " + returned);
			if (watchedMethod.isVoid) {
				return watchedMethod.passThrough;
			}
			return (boolean) returned;
		} catch (Exception e) {
			throw new RuntimeException("Failed to invoke @Watch method with args: " + Arrays.toString(args), e);
		}
	}

	@Override
	public Object methodCalled(Object thiz, String methodSignature, Object superValue, Object[] args) {
		WatchedMethod watchedMethod = watchedMethods.get(methodSignature);
		if (watchedMethod == null) {
			return super.methodCalled(thiz, methodSignature, superValue, args);
		}
		try {
			return watchedMethod.method.invoke(toWatch, args);
		} catch (Exception e) {
			throw new RuntimeException("Failed to invoke @Watch method", e);
		}
	}

	@AllArgsConstructor
	class WatchedMethod {
		String  signature;
		boolean passThrough;
		//		boolean ignoreThiz;
		boolean isVoid;
		Method  method;
	}

}
