package org.inventivetalent.npclib;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.ClassResolver;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.ResolverQuery;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;
import org.inventivetalent.reflection.resolver.minecraft.OBCClassResolver;
import org.inventivetalent.reflection.resolver.wrapper.ClassWrapper;
import org.inventivetalent.reflection.resolver.wrapper.MethodWrapper;
import org.inventivetalent.reflection.util.AccessUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
		//		StringBuilder stringBuilder = new StringBuilder(method.getName());
		//		stringBuilder.append("(");
		//
		//		boolean first = true;
		//		for (Class clazz : method.getParameterTypes()) {
		//			if (!first) { stringBuilder.append(","); }
		//			stringBuilder.append(clazz.getSimpleName());
		//			first = false;
		//		}
		//		return stringBuilder.append(")").toString();
		return MethodWrapper.getMethodSignature(method, false);
	}

	public static String getDamageSourceName(Object damageSource) {
		if (damageSource != null) {
			try {
				return (String) Reflection.nmsClassResolver.resolve("DamageSource").getField("translationIndex").get(damageSource);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	public static EntityDamageEvent.DamageCause damageSourceToCause(String damageName) {
		if (damageName == null) {
			return EntityDamageEvent.DamageCause.VOID;
		}
		EntityDamageEvent.DamageCause cause = EntityDamageEvent.DamageCause.CUSTOM;
		switch (damageName) {
			case "inFire":
				cause = EntityDamageEvent.DamageCause.FIRE;
				break;
			case "onFire":
				cause = EntityDamageEvent.DamageCause.FIRE_TICK;
				break;
			case "lava":
				cause = EntityDamageEvent.DamageCause.LAVA;
				break;
			case "inWall":
				cause = EntityDamageEvent.DamageCause.SUFFOCATION;
				break;
			case "drown":
				cause = EntityDamageEvent.DamageCause.DROWNING;
				break;
			case "starve":
				cause = EntityDamageEvent.DamageCause.STARVATION;
				break;
			case "cactus":
				cause = EntityDamageEvent.DamageCause.CONTACT;
				break;
			case "fall":
				cause = EntityDamageEvent.DamageCause.FALL;
				break;
			case "outOfWorld":
				cause = EntityDamageEvent.DamageCause.VOID;
				break;
			case "generic":
				cause = EntityDamageEvent.DamageCause.CUSTOM;
				break;
			case "indirectMagic":
				cause = EntityDamageEvent.DamageCause.MAGIC;
				break;
			case "magic":
				cause = EntityDamageEvent.DamageCause.POISON;
				break;
			case "wither":
				cause = EntityDamageEvent.DamageCause.WITHER;
				break;
			case "anvil":
			case "fallingBlock":
				cause = EntityDamageEvent.DamageCause.FALLING_BLOCK;
				break;
			case "thorns":
				cause = EntityDamageEvent.DamageCause.THORNS;
				break;
			case "fireball":
			case "arrow":
				cause = EntityDamageEvent.DamageCause.PROJECTILE;
				break;
			case "thrown":// No idea what causes this
			case "mob":
			case "player":
				cause = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
				break;
			case "explosion.player":
			case "explosion":
				cause = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
				break;
			case "dragonBreath":
				cause = EntityDamageEvent.DamageCause.DRAGON_BREATH;
				break;
			case "flyIntoWall":
				cause = EntityDamageEvent.DamageCause.FLY_INTO_WALL;
				break;
			case "hotFloor":
				cause = EntityDamageEvent.DamageCause.HOT_FLOOR;
				break;
			default:
				break;
		}
		return cause;
	}

	public static Entity getEntityFromDamageSource(Object damageSource) {
		if (damageSource == null) { return null; }
		try {
			Class<?> classDamageSource = nmsClassResolver.resolve("DamageSource");
			Object nmsEntity = classDamageSource.getDeclaredMethod("getEntity").invoke(damageSource);
			if (nmsEntity == null) { return null; }
			return Minecraft.getBukkitEntity(nmsEntity);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException();
		}
	}

	public static Entity getEntityById(World world, int id) {
		try {
			Field mapField = AccessUtil.setAccessible(nmsClassResolver.resolve("World").getDeclaredField("entitiesById"));
			Object nmsEntity = nmsClassResolver.resolve("IntHashMap").getDeclaredMethod("get", int.class).invoke(mapField.get(Minecraft.getHandle(world)), id);
			return nmsEntity == null ? null : Minecraft.getBukkitEntity(nmsEntity);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public static void mergeNBTCompound(Object originalTag, Object toMerge) {
		try {
			Class<?> clazz = nmsClassResolver.resolve("NBTTagCompound");
			Method method = new MethodResolver(clazz).resolve(new ResolverQuery("merge", clazz)/*(SRG mapping)*/, new ResolverQuery("a", clazz));
			method.invoke(originalTag, toMerge);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getDataWatcher(org.bukkit.entity.Entity ent) {
		try {
			return nmsClassResolver.resolve("Entity").getDeclaredMethod("getDataWatcher").invoke(Minecraft.getHandle(ent));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
