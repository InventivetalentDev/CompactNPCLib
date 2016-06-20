package org.inventivetalent.npclib.watcher;

import lombok.AllArgsConstructor;
import org.inventivetalent.npclib.ObjectContainer;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.SuperSwitch;

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
					if (!watchedMethods.containsKey(signature)) {
						System.out.println("Watching method " + signature);
						watchedMethods.put(signature, new WatchedMethod(signature, passThrough, /*ignoreThiz,*/method.getReturnType().equals(Void.TYPE), method));
					}
				}
			}
		}
	}

	@Override
	public boolean methodCalled(Object thiz, String methodSignature, ObjectContainer[] args) {
		WatchedMethod watchedMethod = watchedMethods.get(methodSignature);
		if (watchedMethod == null) {
			return super.methodCalled(thiz, methodSignature, args);
		}
		try {
			Object returned = watchedMethod.method.invoke(toWatch, args);
			if (watchedMethod.isVoid) {
				return watchedMethod.passThrough;
			}
			return (boolean) returned;
		} catch (Exception e) {
			throw new RuntimeException("Failed to invoke @Watch method " + methodSignature + " with args: " + Arrays.toString(args), e);
		}
	}

	@Override
	public Object methodCalled(Object thiz, String methodSignature, SuperSwitch superSwitch, ObjectContainer[] args) {
		WatchedMethod watchedMethod = watchedMethods.get(methodSignature);
		if (watchedMethod == null) {
			return super.methodCalled(thiz, methodSignature, superSwitch, args);
		}
		try {
			return watchedMethod.method.invoke(toWatch, args);
		} catch (Exception e) {
			throw new RuntimeException("Failed to invoke @Watch method " + methodSignature + " with args: " + Arrays.toString(args), e);
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
