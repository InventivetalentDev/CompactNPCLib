package org.inventivetalent.npclib.watcher;

import lombok.AllArgsConstructor;
import org.inventivetalent.npclib.NPCLib;
import org.inventivetalent.npclib.ObjectContainer;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.SuperSwitch;
import org.inventivetalent.reflection.resolver.wrapper.MethodWrapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotatedMethodWatcher extends MethodWatcher {

	Object toWatch;
	Map<MethodWrapper.MethodSignature, WatchedMethod> watchedMethods = new ConcurrentHashMap<>();

	public AnnotatedMethodWatcher(Object toWatch) {
		this(toWatch, true);
	}

	public AnnotatedMethodWatcher(Object toWatch, boolean searchSuper) {
		this.toWatch = toWatch;
		if (searchSuper) {
			Class<?> superClazz = toWatch.getClass();
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
				boolean containers = annotation.containers();
				//				boolean ignoreThiz = annotation.ignoreThiz();

				for (String signature : signatures) {
					MethodWrapper.MethodSignature methodSignature = MethodWrapper.MethodSignature.fromString(signature);
					if (!watchedMethods.containsKey(methodSignature)) {
						NPCLib.debug("@Watching method", methodSignature.getSignature(), "in", clazz.getName());
						boolean hasSwitch = false;
						if (method.getParameterTypes().length >= 1) {
							if (SuperSwitch.class.isAssignableFrom(method.getParameterTypes()[method.getParameterTypes().length - 1])) {
								hasSwitch = true;
							}
						}
						boolean hasWildcards = signature.contains("*") || signature.contains("?");
						watchedMethods.put(methodSignature, new WatchedMethod(signature, passThrough, containers,/*ignoreThiz,*/method.getReturnType().equals(Void.TYPE), hasSwitch, hasWildcards, method));
					}
				}
			}
		}
	}

	//	@Override
	//	public boolean methodCalled(Object thiz, String methodSignature, ObjectContainer[] containers) {
	//		WatchedMethod watchedMethod = watchedMethods.get(methodSignature);
	//		if (watchedMethod == null) {
	//			return super.methodCalled(thiz, methodSignature, containers);
	//		}
	//		Object[] args = watchedMethod.containers ? containers : ObjectContainer.toObjects(containers);
	//		try {
	//			Object returned = watchedMethod.method.invoke(toWatch, args);
	//			if (watchedMethod.isVoid) {
	//				return watchedMethod.passThrough;
	//			}
	//			return (boolean) returned;
	//		} catch (Exception e) {
	//			throw new RuntimeException("Failed to invoke @Watch method " + methodSignature + " with args: " + Arrays.toString(args), e);
	//		}
	//	}

	@Override
	public Object methodCalled(Object thiz, String methodSignature, SuperSwitch superSwitch, ObjectContainer[] containers) {
		MethodWrapper.MethodSignature calledSignature = MethodWrapper.MethodSignature.fromString(methodSignature);
		WatchedMethod watchedMethod = null;
		for (Map.Entry<MethodWrapper.MethodSignature, WatchedMethod> entry : this.watchedMethods.entrySet()) {
			if (entry.getKey().matches(calledSignature)) {
				watchedMethod = entry.getValue();
				break;
			}
		}
		if (watchedMethod == null) {
			return super.methodCalled(thiz, methodSignature, superSwitch, containers);
		}
		Object[] args = watchedMethod.containers ? containers : ObjectContainer.toObjects(containers);
		try {
			if (watchedMethod.hasSwitch) {
				args = Arrays.copyOf(args, args.length + 1, Object[].class);
				args[args.length - 1] = superSwitch;
			}
			return watchedMethod.method.invoke(toWatch, args);
		} catch (Exception e) {
			throw new RuntimeException("Failed to invoke @Watch method " + methodSignature + " in " + toWatch.getClass() + " with args: " + Arrays.toString(args), e);
		}
	}

	@AllArgsConstructor
	class WatchedMethod {
		String  signature;
		boolean passThrough;
		boolean containers;
		//		boolean ignoreThiz;
		boolean isVoid;
		boolean hasSwitch;
		boolean hasWildcards;
		Method  method;
	}

}
