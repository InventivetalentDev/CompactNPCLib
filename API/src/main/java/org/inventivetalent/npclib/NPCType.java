package org.inventivetalent.npclib;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.npc.NPCExperienceOrb;
import org.inventivetalent.npclib.npc.NPCItem;
import org.inventivetalent.npclib.npc.living.NPCArmorStand;
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
	DROPPED_ITEM(EntityType.DROPPED_ITEM, NPCItem.class),
	EXPERIENCE_ORB(EntityType.EXPERIENCE_ORB, NPCExperienceOrb.class),
	//	LEASH_KNOT(EntityType.LEASH_HITCH, NPCLeashKnot.class),
	//	PAINTING(EntityType.PAINTING,NPCPainting.class),
	//	ARROW(EntityType.ARROW,NPCArrow.class),
	//	SNOWBALL(EntityType.SNOWBALL,NPCSnowball.class),
	//	LARGE_FIREBALL(EntityType.FIREBALL,NPCLargeFireball.class),
	//	SMALL_FIREBALL(EntityType.SMALL_FIREBALL,NPCSmallFireball.class),
	//	ENDER_PEARL(EntityType.ENDER_PEARL,NPCEnderPearl.class),
	//	ENDER_SIGNAL(EntityType.ENDER_SIGNAL,NPCEnderSignal.class),

	//	THROWN_EXP_BOTTLE(EntityType.THROWN_EXP_BOTTLE,NPCExpBottle.class),
	//	ITEM_FRAME(EntityType.ITEM_FRAME,NPCItemFrame.class),
	//	WITHER_SKULL(EntityType.WITHER_SKULL,NPCWitherSkull.class),
	//	PRIMED_TNT(EntityType.PRIMED_TNT,NPCPrimedTnt.class),
	//	FALLING_BLOCK(EntityType.FALLING_BLOCK,NPCFallingBlock.class),
	//	FIREWORK(EntityType.FIREWORK,NPCFirework.class),
	//	TIPPED_ARROW(EntityType.TIPPED_ARROW,NPCTippedArrow.class),
	//	SPECTRAL_ARROW(EntityType.SPECTRAL_ARROW,NPCSpectralArrow.class),
	//	SHULKER_BULLET(EntityType.SHULKER_BULLET,NPCShulkerBullet.class),
	//	DRAGON_FIREBALL(EntityType.DRAGON_FIREBALL,NPCDragonFireball.class),

	ARMOR_STAND(EntityType.ARMOR_STAND, NPCArmorStand.class),

	//	MINECART_COMMAND(EntityType.MINECART_COMMAND,NPCMinecartCommand.class),
	//	BOAT(EntityType.BOAT,NPCBoat.class),
	//	MINECART_RIDEABLE(EntityType.MINECART,NPCMinecartRideable.class),
	//	MINECART_CHEST(EntityType.MINECART_CHEST,NPCMinecartChest.class),
	//	MINECART_FURNACE(EntityType.MINECART_FURNACE,NPCMinecartFurnace.class),
	//	MINECART_TNT(EntityType.MINECART_TNT,NPCMinecartTnt.class),
	//	MINECART_HOPPER(EntityType.MINECART_HOPPER,NPCMinecartHopper.class),
	//	MINECART_MOB_SPAWNER(EntityType.MINECART_MOB_SPAWNER,NPCMinecartMobSpawner.class),

	CREEPER(EntityType.CREEPER, NPCCreeper.class),
	SKELETON(EntityType.SKELETON, NPCSkeleton.class),
	SPIDER(EntityType.SPIDER, NPCSpider.class),
	GIANT(EntityType.GIANT, NPCGiant.class),
	ZOMBIE(EntityType.ZOMBIE, NPCZombie.class),
	SLIME(EntityType.SLIME, NPCSlime.class),
	GHAST(EntityType.GHAST, NPCGhast.class),
	PIG_ZOMBIE(EntityType.PIG_ZOMBIE, NPCPigZombie.class),
	ENDERMAN(EntityType.ENDERMAN, NPCEnderman.class),
	CAVE_SPIDER(EntityType.CAVE_SPIDER, NPCCaveSpider.class),
	//	SILVERFISH(EntityType.SILVERFISH, NPCSilverfish.class),
	//	BLAZE(EntityType.BLAZE, NPCBlaze.class),
	//	MAGMA_CUBE(EntityType.MAGMA_CUBE, NPCMagmaCube.class),
	ENDER_DRAGON(EntityType.ENDER_DRAGON, NPCEnderDragon.class),
	WITHER(EntityType.WITHER, NPCWither.class),
	//	BAT(EntityType.BAT, NPCBat.class),
	WITCH(EntityType.WITCH, NPCWitch.class),
	ENDERMITE(EntityType.ENDERMITE, NPCEndermite.class),
	GUARDIAN(EntityType.GUARDIAN, NPCGuardian.class),
	SHULKER(EntityType.SHULKER, NPCShulker.class),

	PIG(EntityType.PIG, NPCPig.class),
	SHEEP(EntityType.SHEEP, NPCSheep.class),
	COW(EntityType.COW, NPCCow.class),
	CHICKEN(EntityType.CHICKEN, NPCChicken.class),
	SQUID(EntityType.SQUID, NPCSquid.class),
	WOLF(EntityType.WOLF, NPCWolf.class),
	MUSHROOM_COW(EntityType.MUSHROOM_COW, NPCMushroomCow.class),
	SNOWMAN(EntityType.SNOWMAN, NPCSnowman.class),
	OCELOT(EntityType.OCELOT, NPCOcelot.class),
	IRON_GOLEM(EntityType.IRON_GOLEM, NPCIronGolem.class),
	HORSE(EntityType.HORSE, NPCHorse.class),
	RABBIT(EntityType.RABBIT, NPCRabbit.class),
	POLAR_BEAR(EntityType.POLAR_BEAR, NPCPolarBear.class),

	VILLAGER(EntityType.VILLAGER, NPCVillager.class),

	//	ENDER_CRYSTAL(EntityType.ENDER_CRYSTAL,NPCEnderCrystal.class),
	;

	private static final Map<EntityType, NPCType>                   entityTypeMap = Maps.newHashMap();
	private static final Map<Class<? extends NPCAbstract>, NPCType> classMap      = Maps.newHashMap();

	static {
		for (NPCType npcType : values()) {
			entityTypeMap.put(npcType.entityType, npcType);
			classMap.put(npcType.npcClass, npcType);
		}
	}

	@Getter private final EntityType                   entityType;
	@Getter private       Class<? extends NPCAbstract> npcClass;

	NPCType(EntityType entityType, Class<? extends NPCAbstract> npcClass) {
		this.entityType = entityType;
		this.npcClass = npcClass;
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

}
