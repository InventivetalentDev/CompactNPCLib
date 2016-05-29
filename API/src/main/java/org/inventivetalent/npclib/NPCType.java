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

import com.google.common.collect.Maps;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.entity.living.human.NPCEntity;

import java.util.Map;

public enum NPCType {
	;
//	DROPPED_ITEM("DROPPED_ITEM", "Item", 1, "EntityItem", EntityItem.class),
//	EXPERIENCE_ORB("EXPERIENCE_ORB", "ExperienceOrb", 2, "EntityExperienceOrb", EntityExperienceOrb.class),
//	LEASH_KNOT("LEASH_HITCH", "LeashHitch", 8, "EntityLeash"),
//	PAINTING("PAINTING", "Painting", 9, "EntityPainting"),
//	ARROW("ARROW", "Arrow", 10, "EntityTippedArrow"),
//	SNOWBALL("SNOWBALL", "Snowball", 11, "EntitySnowball"),
//	LARGE_FIREBALL("FIREBALL", "LargeFireball", 12, "EntityLargeFireball"),
//	SMALL_FIREBALL("SMALL_FIREBALL", "SmallFireball", 13, "EntitySmallFireball"),
//	ENDER_PEARL("ENDER_PEARL", "EnderPearl", 14, "EntityEnderPearl"),
//	ENDER_SIGNAL("ENDER_SIGNAL", "EnderSignal", 15, "EntityEnderSignal"),
//
//	THROWN_EXP_BOTTLE("THROWN_EXP_BOTTLE", "ThrownExpBottle", 17, "EntityThrownExpBottle"),
//	ITEM_FRAME("ITEM_FRAME", "ItemFrame", 18, "EntityItemFrame"),
//	WITHER_SKULL("WITHER_SKULL", "WitherSkull", 19, "EntityWitherSkull"),
//	PRIMED_TNT("PRIMED_TNT", "TNTPrimed", 20, "EntityTNTPrimed"),
//	FALLING_BLOCK("FALLING_BLOCK", "FallingBlock", 21, "EntityFallingBlock"),
//	FIREWORK("FIREWORK", "Firework", 22, "EntityFireworks"),
//	TIPPED_ARROW("TIPPED_ARROW", "TippedArrow", 23, "EntityTippedArrow"),
//	SPECTRAL_ARROW("SPECTRAL_ARROW", "SpectralArrow", 24, "EntitySpectralArrow"),
//	SHULKER_BULLET("SHULKER_BULLET", "ShulkerBullet", 25, "EntityShulkerBullet"),
//	DRAGON_FIREBALL("DRAGON_FIREBALL", "DragonFireball", 26, "EntityDragonFireball"),
//
//	ARMOR_STAND("ARMOR_STAND", "ArmorStand", 30, "EntityArmorStand"),
//
//	MINECART_COMMAND("MINECART_COMMAND", "minecart.CommandMinecart", 40, "EntityMinecartCommandBlock"),
//	BOAT("BOAT", "Boat", 41, "EntityBoat"),
//	MINECART_RIDEABLE("MINECART", "minecart.RideableMinecart", 42, "EntityMinecartRideable"),
//	MINECART_CHEST("MINECART_CHEST", "minecart.StorageMinecart", 43, "EntityMinecartChest"),
//	MINECART_FURNACE("MINECART_FURNACE", "minecart.PoweredMinecart", 44, "EntityMinecartFurnace"),
//	MINECART_TNT("MINECART_TNT", "minecart.ExplosiveMinecart", 45, "EntityMinecartTNT"),
//	MINECART_HOPPER("MINECART_HOPPER", "minecart.HopperMinecart", 46, "EntityMinecartHopper"),
//	MINECART_MOB_SPAWNER("MINECART_MOB_SPAWNER", "minecart.SpawnerMinecart", 47, "EntityMinecartMobSpawner"),
//
//	CREEPER("CREEPER", "Creeper", 50, "EntityCreeper"),
//	SKELETON("SKELETON", "Skeleton", 51, "EntitySkeleton"),
//	SPIDER("SPIDER", "Spider", 52, "EntitySpider"),
//	GIANT("GIANT", "Giant", 53, "EntityGiantZombie"),
//	ZOMBIE("ZOMBIE", "Zombie", 54, "EntityZombie"),
//	SLIME("SLIME", "Slime", 55, "EntitySlime"),
//	GHAST("GHAST", "Ghast", 56, "EntityGhast"),
//	PIG_ZOMBIE("PIG_ZOMBIE", "PigZombie", 57, "EntityPigZombie"),
//	ENDERMAN("ENDERMAN", "Enderman", 58, "EntityEnderman"),
//	CAVE_SPIDER("SPIDER", "Spider", 59, "EntityCaveSpider"),
//	SILVERFISH("SILVERFISH", "Silverfish", 60, "EntitySilverfish"),
//	BLAZE("BLAZE", "Blaze", 61, "EntityBlaze"),
//	MAGMA_CUBE("MAGMA_CUBE", "MagmaCube", 62, "EntityMagmaCube"),
//	ENDER_DRAGON("ENDER_DRAGON", "EnderDragon", 63, "EntityEnderDragon"),
//	WITHER("WITHER", "Wither", 64, "EntityWither"),
//	BAT("BAT", "Bat", 65, "EntityBat"),
//	WITCH("WITCH", "Witch", 66, "EntityWitch"),
//	ENDERMITE("ENDERMITE", "Endermite", 67, "EntityEndermite"),
//	GUARDIAN("GUARDIAN", "Guardian", 68, "EntityGuardian"),
//	SHULKER("SHULKER", "Shulker", 69, "EntityShulker"),
//
//	PIG("PIG", "Pig", 90, "EntityPig"),
//	SHEEP("SHEEP", "Sheep", 91, "EntitySheep"),
//	COW("COW", "Cow", 92, "EntityCow"),
//	CHICKEN("CHICKEN", "Chicken", 93, "EntityChicken"),
//	SQUID("SQUID", "Squid", 94, "EntitySquid"),
//	WOLF("WOLF", "Wolf", 95, "EntityWolf"),
//	MUSHROOM_COW("MUSHROOM_COW", "MushroomCow", 96, "EntityMushroomCow"),
//	SNOWMAN("SNOWMAN", "Snowman", 97, "EntitySnowman"),
//	OCELOT("OCELOT", "Ocelot", 98, "EntityOcelot"),
//	IRON_GOLEM("IRON_GOLEM", "IronGolem", 99, "EntityIronGolem"),
//	HORSE("HORSE", "Horse", 100, "EntityHorse"),
//	RABBIT("RABBIT", "Rabbit", 101, "EntityRabbit"),
//
//	VILLAGER("VILLAGER", "Villager", 120, "EntityVillager"),
//
//	ENDER_CRYSTAL("ENDER_CRYSTAL", "EnderCrystal", 200, "EntityEnderCrystal");

	@Getter private final String entityTypeName;
	@Getter private final String entityClassName;
	@Getter private final int    typeId;
	@Getter private final String nmsClassName;
	@Getter private final Class<?> npcInterfaceClass;

	@Getter private final String npcClassName;



	private static final Map<EntityType, NPCType> ENTITY_TYPE_MAP = Maps.newHashMap();

	static {
		//
		//		for (NPCType npcType : values()) {
		//			ENTITY_TYPE_MAP.put(npcType.entityTypeName, npcType);
		//		}
	}

	NPCType(String entityTypeName, String entityClassName, int typeId, String nmsName,Class<? extends NPCEntity> npcInterfaceClass) {
		this.entityTypeName = entityTypeName;
		this.entityClassName = entityClassName;
		this.typeId = typeId;
		this.nmsClassName = nmsName;
		this.npcInterfaceClass = npcInterfaceClass;

		this.npcClassName = "NPC" + nmsName;
	}

	public EntityType getEntityType() {
		return EntityType.valueOf(entityTypeName);
	}

	public Class<? extends Entity> getEntityClass() {
		//noinspection unchecked
		return (Class<? extends Entity>) Reflection.classResolver.resolveSilent("org.bukkit.entity." + entityClassName);
	}

	public Class<?> getNMSClass() {
		try {
			return Reflection.nmsClassResolver.resolve(nmsClassName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public Class<?> getNPCClass() {
		return Reflection.getVersionedClass("org.inventivetalent.npc.entity", this.npcClassName).getClazz();
	}

	public static NPCType fromEntityType(EntityType entityType) {
		return ENTITY_TYPE_MAP.get(entityType);
	}

}
