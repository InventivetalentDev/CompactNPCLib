package org.inventivetalent.npclib;

import com.google.common.base.Joiner;
import com.google.common.primitives.Primitives;
import com.google.common.reflect.Invokable;
import javassist.*;
import org.inventivetalent.npclib.annotation.ExtraMethod;
import org.inventivetalent.npclib.annotation.NPCInfo;
import org.inventivetalent.npclib.entity.NPCEntity;
import org.inventivetalent.reflection.minecraft.Minecraft;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ClassGenerator {

	public static Class<?> generateEntityClass(ClassPool classPool, NPCInfo npcInfo) throws ReflectiveOperationException, CannotCompileException, NotFoundException {
		classPool.insertClassPath(new LoaderClassPath(ClassGenerator.class.getClassLoader()));

		CtClass generated = classPool.makeClass("org.inventivetalent.npc.entity.generated." + npcInfo.getNPCClassName());

		CtClass npcInterface = classPool.get(npcInfo.getEntity().getName());
		generated.setInterfaces(new CtClass[] { npcInterface });
		generated.setSuperclass(classPool.get("net.minecraft.server." + Minecraft.VERSION.name() + "." + npcInfo.getNms()));

		classPool.importPackage("net.minecraft.server." + Minecraft.VERSION.name());
		classPool.importPackage("org.inventivetalent.npclib");
		classPool.importPackage("org.inventivetalent.npclib.watcher");
		classPool.importPackage("org.inventivetalent.npclib.npc");
		classPool.importPackage("org.inventivetalent.npclib.annotation");
		classPool.importPackage("org.inventivetalent.npclib.entity");
		classPool.importPackage("org.inventivetalent.npclib.entity.generated");

		for (String packge : npcInfo.getExtraPackages()) {
			classPool.importPackage(packge);
		}

		generated.addField(CtField.make("public NPCAbstract $npc;", generated));
		generated.addField(CtField.make("public NPCInfo $npcInfo;", generated));
		generated.addField(CtField.make("public MethodWatcher $methodWatcher;", generated));
		generated.addMethod(CtMethod.make("public void setMethodWatcher(MethodWatcher methodWatcher) {\n" +
				"  this.$methodWatcher=methodWatcher;\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public void setNpcInfo(NPCInfo npcInfo) {\n"
				+ "  this.$npcInfo=npcInfo;\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("public NPCInfo getNpcInfo() {\n"
				+ "  return this.$npcInfo;\n"
				+ "}", generated));

		// Constructor
		//		generated.addConstructor(CtNewConstructor.make("public " + npcInfo.getNPCClassName() + "(World world){\n"
		//				+ "  super(world);\n"//TODO notify about constructor
		//				+ "}", generated));
		//		generated.addConstructor(CtNewConstructor.make("public " + npcInfo.getNPCClassName() + "(World world, MethodWatcher methodWatcher){\n"
		//				+ "  super(world);\n"//TODO notify about constructor
		//				+ "  this.$methodWatcher=methodWatcher;\n"
		//				+ "}", generated));
		for (String constructor : npcInfo.getConstructors()) {
			generated.addConstructor(CtNewConstructor.make(String.format(constructor, npcInfo.getNPCClassName()), generated));
		}

		generated.addMethod(CtMethod.make("public boolean methodCalled(String name, ObjectContainer[] args) {\n"
				+ "  if(this.$methodWatcher == null) return true;\n"
				+ "  return this.$methodWatcher.methodCalled(this, name, args);\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("public Object methodCalled(String name, SuperSwitch superSwitch, ObjectContainer[] args) {\n"
				+ "  if(this.$methodWatcher == null) return null;\n"
				+ "  return this.$methodWatcher.methodCalled(this, name, superSwitch, args);\n"
				+ "}", generated));

		// Primitive types
		generated.addMethod(CtMethod.make("public byte methodCalled_byte(String name, SuperSwitch superSwitch, ObjectContainer[] args) {\n" +
				"  Object $returned = this.methodCalled(name, superSwitch, args);\n" +
				"  if($returned==null)return 0;" +
				"  return ((Byte) $returned).byteValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public short methodCalled_short(String name, SuperSwitch superSwitch, ObjectContainer[] args) {\n" +
				"  Object $returned = this.methodCalled(name, superSwitch, args);\n" +
				"  if($returned==null)return 0;" +
				"  return ((Short) $returned).shortValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public int methodCalled_int(String name, SuperSwitch superSwitch, ObjectContainer[] args) {\n" +
				"  Object $returned = this.methodCalled(name, superSwitch, args);\n" +
				"  if($returned==null)return 0;" +
				"  return ((Integer) $returned).intValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public long methodCalled_long(String name, SuperSwitch superSwitch, ObjectContainer[] args) {\n" +
				"  Object $returned = this.methodCalled(name, superSwitch, args);\n" +
				"  if($returned==null)return 0L;" +
				"  return ((Long) $returned).longValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public float methodCalled_float(String name, SuperSwitch superSwitch, ObjectContainer[] args) {\n" +
				"  Object $returned = this.methodCalled(name, superSwitch, args);\n" +
				"  if($returned==null)return 0.0f;" +
				"  return ((Float) $returned).floatValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public double methodCalled_double(String name, SuperSwitch superSwitch, ObjectContainer[] args) {\n" +
				"  Object $returned = this.methodCalled(name, superSwitch, args);\n" +
				"  if($returned==null)return 0.0d;" +
				"  return ((Double) $returned).doubleValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public boolean methodCalled_boolean(String name, SuperSwitch superSwitch, ObjectContainer[] args) {\n" +
				"  Object $returned = this.methodCalled(name, superSwitch, args);\n" +
				"  if($returned==null)return false;" +
				"  return ((Boolean) $returned).booleanValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public char methodCalled_char(String name, SuperSwitch superSwitch, ObjectContainer[] args) {\n" +
				"  Object $returned = this.methodCalled(name, superSwitch, args);\n" +
				"  if($returned==null)return '\\u0000';" +
				"  return ((Character) $returned).charValue();\n" +
				"}", generated));
		//TODO

		// @ExtraMethods in the entity interface
		Class superInterface = npcInfo.getEntity();
		while (superInterface != null) {
			for (Method method : superInterface.getMethods()) {
				ExtraMethod annotation = method.getAnnotation(ExtraMethod.class);
				if (annotation != null) {
					generated.addMethod(CtMethod.make(annotation.value(), generated));
				}
			}
			superInterface = superInterface.getSuperclass();
		}

		// Extra fields & methods
		for (String field : npcInfo.getExtraFields()) {
			generated.addField(CtField.make(field, generated));
		}
		for (String method : npcInfo.getExtraMethods()) {
			generated.addMethod(CtMethod.make(method, generated));
		}

		Map<String, Method> overridableMethods = new HashMap<>();
		Set<String> ignoredMethods = new HashSet<>();

		Class entityClass = npcInfo.getNMSClass();
		while (entityClass != null) {
			for (Method method : entityClass.getDeclaredMethods()) {
				String signature = method.getName() + Arrays.toString(method.getParameterTypes());
				if (!Invokable.from(method).isOverridable()) {
					overridableMethods.remove(signature);
					ignoredMethods.add(signature);
					continue;
				}

				if (!overridableMethods.containsKey(signature) && !ignoredMethods.contains(signature)) {
					overridableMethods.put(signature, method);
				}
			}
			entityClass = entityClass.getSuperclass();
		}

		//		entityClass = npcType.getNMSClass();
		//			for (Method method : entityClass.getDeclaredMethods()) {
		//				String signature = method.getName() + Arrays.toString(method.getParameterTypes());
		//				if (!Invokable.from(method).isOverridable()) {
		//					overridableMethods.remove(signature);
		//					ignoredMethods.add(signature);
		//					continue;
		//				}
		//
		//				if (!overridableMethods.containsKey(signature) && !ignoredMethods.contains(signature)) {
		//					overridableMethods.put(signature, method);
		//				}
		//			}
		//
		//		for (Iterator<Map.Entry<String, Method>> iterator = overridableMethods.entrySet().iterator(); iterator.hasNext(); ) {
		//			Map.Entry<String, Method> next = iterator.next();
		//			Invokable invokable = Invokable.from(next.getValue());
		//			if (!invokable.isOverridable()) { iterator.remove(); }
		//		}

		//		System.out.println(overridableMethods.keySet());

		for (Method method : overridableMethods.values()) {
			//			System.out.println(method);
			//			System.out.println("final: " + Modifier.isFinal(method.getModifiers()));
			if (Modifier.isPrivate(method.getModifiers()) || Modifier.isFinal(method.getModifiers()) || Modifier.isStatic(method.getModifiers()) || Modifier.isAbstract(method.getModifiers()) || Modifier.isNative(method.getModifiers())) {
				continue;
			}

			CtMethod override = makeOverrideMethod(method, generated);
			//			System.out.println(override);
			generated.addMethod(override);
		}

		//		System.out.println(generated.toString());
		//		System.out.println(" \n \n"
		//				+ " \n");
		//		//		try {
		//		//			System.out.println(new String(generated.toBytecode()));
		//		//		} catch (IOException e) {
		//		//			e.printStackTrace();
		//		//		}
		//		System.out.println(" \n \n"
		//				+ " \n");
		try {
			generated.writeFile("generated");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return generated.toClass(NPCEntity.class.getClassLoader(), NPCEntity.class.getProtectionDomain());
	}

	public static Class<?> generatePlayerConnection(ClassPool classPool) throws ReflectiveOperationException, CannotCompileException, NotFoundException {
		classPool.insertClassPath(new LoaderClassPath(ClassGenerator.class.getClassLoader()));

		CtClass generated = classPool.makeClass("org.inventivetalent.npc.entity.generated.player.NPCPlayerConnection");

		CtClass connectionInterface = classPool.get(INPCPlayerConnection.class.getName());
		generated.setInterfaces(new CtClass[] { connectionInterface });
		generated.setSuperclass(classPool.get("net.minecraft.server." + Minecraft.VERSION.name() + ".PlayerConnection"));

		classPool.importPackage("net.minecraft.server." + Minecraft.VERSION.name());
		classPool.importPackage("org.inventivetalent.npclib");
		classPool.importPackage("org.inventivetalent.npclib.npc");
		classPool.importPackage("org.inventivetalent.npclib.entity");
		classPool.importPackage("org.inventivetalent.npclib.entity.generated");
		classPool.importPackage("org.inventivetalent.npclib.entity.generated.player");

		generated.addConstructor(CtNewConstructor.make("public NPCPlayerConnection(MinecraftServer minecraftServer, NetworkManager networkManager, EntityPlayer entityPlayer) {\n"
				+ "  super(minecraftServer, networkManager, entityPlayer);\n"
				+ "}", generated));

		generated.addMethod(CtMethod.make("public void sendPacket(Packet packet) {}", generated));// TODO: NPCPacketEvent?

		try {
			generated.writeFile("generated");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return generated.toClass(INPCPlayerConnection.class.getClassLoader(), INPCPlayerConnection.class.getProtectionDomain());
	}

	public static Class<?> generateChannel(ClassPool classPool) throws ReflectiveOperationException, CannotCompileException, NotFoundException {
		classPool.insertClassPath(new LoaderClassPath(ClassGenerator.class.getClassLoader()));

		CtClass generated = classPool.makeClass("org.inventivetalent.npc.entity.generated.netty.NPCChannel");

		CtClass channelInterface = classPool.get(INPCChannel.class.getName());
		generated.setInterfaces(new CtClass[] { channelInterface });
		generated.setSuperclass(classPool.get("io.netty.channel.AbstractChannel"));

		classPool.importPackage("net.minecraft.server." + Minecraft.VERSION.name());
		classPool.importPackage("io.netty.channel");
		//		classPool.importPackage("java.net");
		classPool.importPackage("org.inventivetalent.npclib");
		classPool.importPackage("org.inventivetalent.npclib.npc");
		classPool.importPackage("org.inventivetalent.npclib.entity");
		classPool.importPackage("org.inventivetalent.npclib.entity.generated");
		classPool.importPackage("org.inventivetalent.npclib.entity.generated.netty");

		generated.addConstructor(CtNewConstructor.make("public NPCChannel(io.netty.channel.Channel parent) {"
				+ "  super(parent);\n"
				+ "}", generated));
		generated.addConstructor(CtNewConstructor.make("public NPCChannel() {\n"
				+ "  super(null);\n"
				+ "}", generated));

		generated.addField(CtField.make("private final ChannelConfig config = new DefaultChannelConfig(this);", generated));
		generated.addField(CtField.make("private boolean open = false;", generated));

		generated.addMethod(CtMethod.make("public ChannelConfig config() {"
				+ "  this.config.setAutoRead(true);\n"
				+ "  return this.config;\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("public void setOpen(boolean open) {"
				+ "  this.open = open;\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("public boolean isOpen() {"
				+ "  return this.open;\n"
				+ "}", generated));
		// Dummy methods
		generated.addMethod(CtMethod.make("public boolean isActive() {\n"
				+ "  return false;\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("public ChannelMetadata metadata() {\n"
				+ "  return null;\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("protected io.netty.channel.AbstractChannel.AbstractUnsafe newUnsafe() {\n"
				+ "  return null;\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("protected boolean isCompatible(EventLoop eventloop) {\n"
				+ "  return false;\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("protected java.net.SocketAddress localAddress0() {\n"
				+ "  return null;\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("protected java.net.SocketAddress remoteAddress0() {\n"
				+ "  return null;\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("protected void doBind(java.net.SocketAddress socketAddress) throws Exception {}", generated));
		generated.addMethod(CtMethod.make("protected void doDisconnect() throws Exception {}", generated));
		generated.addMethod(CtMethod.make("protected void doClose() throws Exception {}", generated));
		generated.addMethod(CtMethod.make("protected void doBeginRead() throws Exception {}", generated));
		generated.addMethod(CtMethod.make("protected void doWrite(ChannelOutboundBuffer channelOutboundBuffer) throws Exception {}", generated));

		try {
			generated.writeFile("generated");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return generated.toClass(INPCChannel.class.getClassLoader(), INPCChannel.class.getProtectionDomain());
	}

	static CtMethod makeOverrideMethod(Method method, CtClass declaring) throws CannotCompileException {
		String access = Modifier.toString(method.getModifiers());
		String returnType = method.getReturnType().getCanonicalName();
		String name = method.getName();
		Class<?>[] parameterTypes = method.getParameterTypes();
		String signature = Reflection.getMethodSignature(method);

		boolean isVoid = method.getReturnType().equals(Void.TYPE);
		if (isVoid) {
			returnType = "void";
		}

		List<String> paramStrings = new ArrayList<>();
		List<String> callStrings = new ArrayList<>();
		List<String> objectCallStrings = new ArrayList<>();
		List<String> superCallStrings = new ArrayList<>();
		int c = 0;
		for (Class<?> clazz : parameterTypes) {
			paramStrings.add(String.format("%1$s param%2$s", clazz.getCanonicalName(), c));
			callStrings.add(String.format("param%1$s", c));
			objectCallStrings.add(String.format("ObjectConverter.toObject(param%1$s)", c));
			superCallStrings.add(String.format("(%1$s) $mArgs[%2$s]", clazz.getCanonicalName(), c));
			c++;
		}

		Joiner joiner = Joiner.on(",");
		String paramString = joiner.join(paramStrings);
		String callString = joiner.join(callStrings);
		String objectCallString = joiner.join(objectCallStrings);
		String superCallString = joiner.join(superCallStrings);

		String methodString = String.format("%1$s %2$s %3$s(%4$s) {\n",// Override method
				access, returnType, name, paramString);
		System.out.println(methodString);
		System.out.println(Reflection.getMethodSignature(method));

		/// Edit: Javassist uses $args as a special variable, which is probably why you can't assign it again
		/// 
		/// Note to self:
		/// Creating a variable
		/// ("Object[] $args = ...")
		/// and trying to later re-assign it
		/// ("$args = ...")
		/// apparently causes a VerfiyError so
		/// DON'T DO IT!!
		/// (Just a reminder: you spent 3 hours trying to figure this out...)

		methodString += String.format("  ObjectContainer[] $cArgs = ObjectContainer.fromObjects(ArrayMaker.fromParameters(%s));\n", objectCallString);
		methodString += "  SuperSwitch $switch = SuperSwitch.newInstance();\n";

		String primitiveSuffix = "";
		String primitiveNull = "null";
		if (!isVoid && method.getReturnType().isPrimitive()) {
			primitiveSuffix = "_" + method.getReturnType().getName();

			if (Byte.TYPE.equals(method.getReturnType()) || Short.TYPE.equals(method.getReturnType()) || Integer.TYPE.equals(method.getReturnType())) {
				primitiveNull = "0";
			}
			if (Long.TYPE.equals(method.getReturnType())) {
				primitiveNull = "0L";
			}
			if (Float.TYPE.equals(method.getReturnType())) {
				primitiveNull = "0.0f";
			}
			if (Double.TYPE.equals(method.getReturnType())) {
				primitiveNull = "0.0d";
			}
			if (Character.TYPE.isAssignableFrom(method.getReturnType())) {
				primitiveNull = "'\\u0000'";
			}
			if (Boolean.TYPE.isAssignableFrom(method.getReturnType())) {
				primitiveNull = "false";
			}
		}

		if (isVoid) {
			methodString += String.format("  if(!this.methodCalled%2$s(\"%1$s\", $cArgs)) {\n"// Check if super should be called
					+ "    return;\n"// Otherwise return
					+ "  }\n", signature, primitiveSuffix
			);
			methodString += "  Object[] $mArgs = ObjectContainer.toObjects($cArgs);\n";
			methodString += reAssignContainers(parameterTypes);
			methodString += String.format("  super.%1$s(%2$s);\n", name, callString); // Call super
		} else {
			System.out.println(returnType);
			System.out.println(method);
			methodString += String.format(""
					+ "  %3$s $returned = (%3$s) this.methodCalled%5$s(\"%4$s\", $switch, $cArgs);\n"
					+ "  Object[] $mArgs = ObjectContainer.toObjects($cArgs);\n"
					+ reAssignContainers(parameterTypes)
					+ "  if($switch.callSuper()) {\n"
					+ "    if($switch.isReplace()) {\n"
					+ "      return (%3$s) $returned;"
					+ "    } else {\n"
					+ "      return (%3$s) super.%1$s(%2$s);\n"
					+ "    }\n"
					+ "  } else {\n"
					+ "    return (%3$s) %6$s;\n"
					+ "  }\n", name, callString, returnType, signature, primitiveSuffix, primitiveNull);
			//			methodString += String.format("  return (%4$s) super.%1$s(%2$s);\n", name, callString, objectCallString, returnType);
		}
		methodString += "}";

		//		System.out.println(methodString);
		CtMethod ctMethod = CtMethod.make(methodString, declaring);
		return ctMethod;
	}

	static String reAssignContainers(Class<?>[] paramTypes) {
		StringBuilder stringBuilder = new StringBuilder();
		int i = 0;
		for (Class c : paramTypes) {
			stringBuilder.append("param").append(i).append(" = (");
			if (c.isPrimitive()) {
				Class<?> wrapped = Primitives.wrap(c);
				stringBuilder
						.append("(").append(wrapped.getCanonicalName()).append(") $mArgs[").append(i).append("]).")
						.append(c.getCanonicalName()).append("Value();\n");// <-- This feels so cheaty
			} else {
				stringBuilder.append(c.getCanonicalName()).append(") $mArgs[").append(i).append("];\n");
			}
			i++;
		}
		return stringBuilder.toString();
	}

	//	String makeParameters(Class<?>[] parameterTypes) {
	//		Set<String> paramStrings = new HashSet<>();
	//		Set<String> callStrings = new HashSet<>();
	//		int c = 0;
	//		for (Class<?> clazz : parameterTypes) {
	//			paramStrings.add(String.format("%1$s param%2$s", clazz.getName(), c));
	//			callStrings.add(String.format("param%1$s", c));
	//			c++;
	//		}
	//
	//		Joiner joiner = Joiner.on(",");
	//		String paramString = joiner.join(paramStrings);
	//		String callString = joiner.join(callStrings);
	//	}

}
