package org.inventivetalent.npclib;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.npc.NPCExperienceOrb;
import org.inventivetalent.npclib.npc.NPCItem;
import org.inventivetalent.npclib.npc.living.NPCArmorStand;
import org.inventivetalent.npclib.npc.living.human.NPCPlayer;
import org.inventivetalent.npclib.npc.living.insentient.NPCEnderDragon;
import org.inventivetalent.npclib.npc.living.insentient.NPCPigZombie;
import org.inventivetalent.npclib.npc.living.insentient.NPCSlime;
import org.inventivetalent.npclib.npc.living.insentient.NPCZombie;
import org.inventivetalent.npclib.npc.living.insentient.creature.ageable.NPCVillager;
import org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal.*;
import org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal.tameable.NPCOcelot;
import org.inventivetalent.npclib.npc.living.insentient.creature.ageable.animal.tameable.NPCWolf;
import org.inventivetalent.npclib.npc.living.insentient.creature.golem.NPCIronGolem;
import org.inventivetalent.npclib.npc.living.insentient.creature.golem.NPCShulker;
import org.inventivetalent.npclib.npc.living.insentient.creature.golem.NPCSnowman;
import org.inventivetalent.npclib.npc.living.insentient.creature.monster.*;
import org.inventivetalent.npclib.npc.living.insentient.flying.NPCGhast;
import org.inventivetalent.npclib.npc.living.insentient.water.NPCSquid;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public enum NPCType {
	DROPPED_ITEM("DROPPED_ITEM", NPCItem.class),
	EXPERIENCE_ORB("EXPERIENCE_ORB", NPCExperienceOrb.class),
	//	LEASH_KNOT("LEASH_HITCH", NPCLeashKnot.class),
	//	PAINTING("PAINTING",NPCPainting.class),
	//	ARROW("ARROW",NPCArrow.class),
	//	SNOWBALL("SNOWBALL",NPCSnowball.class),
	//	LARGE_FIREBALL("FIREBALL",NPCLargeFireball.class),
	//	SMALL_FIREBALL("SMALL_FIREBALL",NPCSmallFireball.class),
	//	ENDER_PEARL("ENDER_PEARL",NPCEnderPearl.class),
	//	ENDER_SIGNAL("ENDER_SIGNAL",NPCEnderSignal.class),

	//	THROWN_EXP_BOTTLE("THROWN_EXP_BOTTLE",NPCExpBottle.class),
	//	ITEM_FRAME("ITEM_FRAME",NPCItemFrame.class),
	//	WITHER_SKULL("WITHER_SKULL",NPCWitherSkull.class),
	//	PRIMED_TNT("PRIMED_TNT",NPCPrimedTnt.class),
	//	FALLING_BLOCK("FALLING_BLOCK",NPCFallingBlock.class),
	//	FIREWORK("FIREWORK",NPCFirework.class),
	//	TIPPED_ARROW("TIPPED_ARROW",NPCTippedArrow.class),
	//	SPECTRAL_ARROW("SPECTRAL_ARROW",NPCSpectralArrow.class),
	//	SHULKER_BULLET("SHULKER_BULLET",NPCShulkerBullet.class),
	//	DRAGON_FIREBALL("DRAGON_FIREBALL",NPCDragonFireball.class),

	ARMOR_STAND("ARMOR_STAND", NPCArmorStand.class),

	//	MINECART_COMMAND("MINECART_COMMAND",NPCMinecartCommand.class),
	//	BOAT("BOAT",NPCBoat.class),
	//	MINECART_RIDEABLE("MINECART",NPCMinecartRideable.class),
	//	MINECART_CHEST("MINECART_CHEST",NPCMinecartChest.class),
	//	MINECART_FURNACE("MINECART_FURNACE",NPCMinecartFurnace.class),
	//	MINECART_TNT("MINECART_TNT",NPCMinecartTnt.class),
	//	MINECART_HOPPER("MINECART_HOPPER",NPCMinecartHopper.class),
	//	MINECART_MOB_SPAWNER("MINECART_MOB_SPAWNER",NPCMinecartMobSpawner.class),

	CREEPER("CREEPER", NPCCreeper.class),
	SKELETON("SKELETON", NPCSkeleton.class),
	SPIDER("SPIDER", NPCSpider.class),
	GIANT("GIANT", NPCGiant.class),
	ZOMBIE("ZOMBIE", NPCZombie.class),
	SLIME("SLIME", NPCSlime.class),
	GHAST("GHAST", NPCGhast.class),
	PIG_ZOMBIE("PIG_ZOMBIE", NPCPigZombie.class),
	ENDERMAN("ENDERMAN", NPCEnderman.class),
	CAVE_SPIDER("CAVE_SPIDER", NPCCaveSpider.class),
	//	SILVERFISH("SILVERFISH", NPCSilverfish.class),
	//	BLAZE("BLAZE", NPCBlaze.class),
	//	MAGMA_CUBE("MAGMA_CUBE", NPCMagmaCube.class),
	ENDER_DRAGON("ENDER_DRAGON", NPCEnderDragon.class),
	WITHER("WITHER", NPCWither.class),
	//	BAT("BAT", NPCBat.class),
	WITCH("WITCH", NPCWitch.class),
	ENDERMITE("ENDERMITE", NPCEndermite.class),
	GUARDIAN("GUARDIAN", NPCGuardian.class),
	SHULKER("SHULKER", NPCShulker.class),

	PIG("PIG", NPCPig.class),
	SHEEP("SHEEP", NPCSheep.class),
	COW("COW", NPCCow.class),
	CHICKEN("CHICKEN", NPCChicken.class),
	SQUID("SQUID", NPCSquid.class),
	WOLF("WOLF", NPCWolf.class),
	MUSHROOM_COW("MUSHROOM_COW", NPCMushroomCow.class),
	SNOWMAN("SNOWMAN", NPCSnowman.class),
	OCELOT("OCELOT", NPCOcelot.class),
	IRON_GOLEM("IRON_GOLEM", NPCIronGolem.class),
	HORSE("HORSE", NPCHorse.class),
	RABBIT("RABBIT", NPCRabbit.class),
	POLAR_BEAR("POLAR_BEAR", NPCPolarBear.class),

	VILLAGER("VILLAGER", NPCVillager.class),

	//	ENDER_CRYSTAL("ENDER_CRYSTAL",NPCEnderCrystal.class),

	PLAYER("PLAYER", NPCPlayer.class),;

	private static final Map<EntityType, NPCType>                   entityTypeMap = Maps.newHashMap();
	private static final Map<Class<? extends NPCAbstract>, NPCType> classMap      = Maps.newHashMap();

	static {
		for (NPCType npcType : values()) {
			if (npcType.entityType != null) {
				entityTypeMap.put(npcType.entityType, npcType);
			}
			if (npcType.npcClass != null) {
				classMap.put(npcType.npcClass, npcType);
			}
		}
	}

	@Getter private EntityType                   entityType;
	@Getter private Class<? extends NPCAbstract> npcClass;

	NPCType(EntityType entityType, Class<? extends NPCAbstract> npcClass) {
		this.entityType = entityType;
		this.npcClass = npcClass;
	}

	NPCType(String entityTypeName, Class<? extends NPCAbstract> npcClass) {
		try {
			this.entityType = EntityType.valueOf(entityTypeName);
			this.npcClass = npcClass;
		} catch (NoSuchFieldError | IllegalArgumentException ignored) {
			NPCLib.logger.warning("NPCType " + name() + " is not supported on this server version");
		}
	}

	public static NPCType forEntityType(EntityType entityType) {
		return entityTypeMap.get(checkNotNull(entityType));
	}

	public static NPCType forNpcClass(Class<? extends NPCAbstract> clazz) {
		return classMap.get(clazz);
	}

	public static NPCType fromString(String string) {
		if (Strings.isNullOrEmpty(string)) { return null; }
		NPCType type;
		if ((type = valueOfOrNull(string.toUpperCase())) != null) {
			return type;
		}
		if ((type = valueOfOrNull(string.toUpperCase().replace(" ", "_"))) != null) {
			return type;
		}
		if ((type = valueOfOrNull(string.toUpperCase().replaceAll("\\s", ""))) != null) {
			return type;
		}
		for (NPCType npcType : values()) {
			String combined = npcType.name().replace("_", "");
			if (combined.equals(string.toUpperCase())) {
				return npcType;
			}
		}
		return null;
	}

	private static NPCType valueOfOrNull(String string) {
		try {
			return valueOf(string);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Check if this NPC type is available for the current server version
	 *
	 * @return whether this type is available
	 */
	public boolean isAvailable() {
		return this.entityType != null && this.npcClass != null;
	}

}
