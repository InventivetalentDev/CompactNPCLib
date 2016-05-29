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

import com.google.common.base.Joiner;
import com.google.common.reflect.Invokable;
import javassist.*;
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
		classPool.importPackage("org.inventivetalent.npclib.entity");
		classPool.importPackage("org.inventivetalent.npclib.entity.generated");

		for (String packge : npcInfo.getExtraPackages()) {
			classPool.importPackage(packge);
		}

		generated.addField(CtField.make("public NPCAbstract $npc;", generated));
		generated.addField(CtField.make("public MethodWatcher $methodWatcher;", generated));
		generated.addMethod(CtMethod.make("public void setMethodWatcher(MethodWatcher methodWatcher) {\n" +
				"  this.$methodWatcher=methodWatcher;\n" +
				"}", generated));

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

		generated.addMethod(CtMethod.make("public boolean methodCalled(String name, Object[] args) {\n"
				+ "  if(this.$methodWatcher == null) return true;\n"
				+ "  return this.$methodWatcher.methodCalled(this, name, args);\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("public Object methodCalled(String name, Object superValue, Object[] args) {\n"
				+ "  if(this.$methodWatcher == null) return superValue;\n"
				+ "  return this.$methodWatcher.methodCalled(this, name, superValue, args);\n"
				+ "}", generated));

		// Primitive types
		generated.addMethod(CtMethod.make("public byte methodCalled(String name, byte superValue, Object[] args) {\n" +
				"  return ((Byte) this.methodCalled(name, Byte.valueOf(superValue), args)).byteValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public short methodCalled(String name, short superValue, Object[] args) {\n" +
				"  return ((Short) this.methodCalled(name, Short.valueOf(superValue), args)).shortValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public int methodCalled(String name, int superValue, Object[] args) {\n" +
				"  return ((Integer) this.methodCalled(name, Integer.valueOf(superValue), args)).intValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public long methodCalled(String name, long superValue, Object[] args) {\n" +
				"  return ((Long) this.methodCalled(name, Long.valueOf(superValue), args)).longValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public float methodCalled(String name, float superValue, Object[] args) {\n" +
				"  return ((Float) this.methodCalled(name, Float.valueOf(superValue), args)).floatValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public double methodCalled(String name, double superValue, Object[] args) {\n" +
				"  return ((Double) this.methodCalled(name, Double.valueOf(superValue), args)).doubleValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public boolean methodCalled(String name, boolean superValue, Object[] args) {\n" +
				"  return ((Boolean) this.methodCalled(name, Boolean.valueOf(superValue), args)).booleanValue();\n" +
				"}", generated));
		generated.addMethod(CtMethod.make("public char methodCalled(String name, char superValue, Object[] args) {\n" +
				"  return ((Character) this.methodCalled(name, Character.valueOf(superValue), args)).charValue();\n" +
				"}", generated));
		//TODO

		// Helper methods
		generated.addMethod(CtMethod.make("public void spawn() {\n"
				+ "  this.world.addEntity(this);\n"
				+ "}", generated));
		generated.addMethod(CtMethod.make("public void spawn(org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason spawnReason) {\n"
				+ "  this.world.addEntity(this, spawnReason);\n"
				+ "}", generated));

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

		System.out.println(overridableMethods.keySet());

		for (Method method : overridableMethods.values()) {
			System.out.println(method);
			System.out.println("final: " + Modifier.isFinal(method.getModifiers()));
			if (Modifier.isPrivate(method.getModifiers()) || Modifier.isFinal(method.getModifiers()) || Modifier.isStatic(method.getModifiers()) || Modifier.isAbstract(method.getModifiers()) || Modifier.isNative(method.getModifiers())) {
				continue;
			}

			CtMethod override = makeOverrideMethod(method, generated);
			System.out.println(override);
			generated.addMethod(override);
		}

		System.out.println(generated.toString());
		System.out.println(" \n \n"
				+ " \n");
		//		try {
		//			System.out.println(new String(generated.toBytecode()));
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		System.out.println(" \n \n"
				+ " \n");
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

		boolean isVoid = method.getReturnType().equals(Void.TYPE);
		if (isVoid) {
			returnType = "void";
		}

		List<String> paramStrings = new ArrayList<>();
		List<String> callStrings = new ArrayList<>();
		List<String> objectCallStrings = new ArrayList<>();
		int c = 0;
		for (Class<?> clazz : parameterTypes) {
			paramStrings.add(String.format("%1$s param%2$s", clazz.getCanonicalName(), c));
			callStrings.add(String.format("param%1$s", c));
			objectCallStrings.add(String.format("ObjectConverter.toObject(param%1$s)", c));
			c++;
		}

		Joiner joiner = Joiner.on(",");
		String paramString = joiner.join(paramStrings);
		String callString = joiner.join(callStrings);
		String objectCallString = joiner.join(objectCallStrings);

		String methodString = String.format("%1$s %2$s %3$s(%4$s) {\n",// Override method
				access, returnType, name, paramString);
		System.out.println(methodString);

		methodString += String.format("Object[] $args = ArrayMaker.fromParameters(%s);\n", objectCallString);

		if (isVoid) {
			methodString += String.format("  if(!this.methodCalled(\"%1$s\", $args)) {\n"// Check if super should be called
					+ "    return;\n"// Otherwise return
					+ "  }\n", name
			);
			methodString += String.format("  super.%1$s(%2$s);\n", name, callString); // Call super
		} else {
			methodString += String.format("  return (%3$s) this.methodCalled(\"%1$s\", super.%1$s(%2$s), $args);\n", name, callString, returnType);
			//			methodString += String.format("  return (%4$s) super.%1$s(%2$s);\n", name, callString, objectCallString, returnType);
		}
		methodString += "}";

		//		System.out.println(methodString);
		CtMethod ctMethod = CtMethod.make(methodString, declaring);
		return ctMethod;
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
