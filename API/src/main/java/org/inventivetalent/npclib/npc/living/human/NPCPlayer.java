package org.inventivetalent.npclib.npc.living.human;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.mcwrapper.auth.GameProfileWrapper;
import org.inventivetalent.nbt.CompoundTag;
import org.inventivetalent.npclib.ClassBuilder;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.SuperSwitch;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.human.EntityPlayer;
import org.inventivetalent.npclib.event.NPCSpawnEvent;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.ResolverQuery;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

@NPC(id = 1337,
	 type = EntityType.PLAYER,
	 bukkit = Player.class,
	 nms = "EntityPlayer",
	 entity = EntityPlayer.class,
	 extraPackages = {
			 "com.mojang.authlib",
			 "org.bukkit",
			 "org.bukkit.craftbukkit.{{version}}",
			 "java.util"
	 },
	 constructors = {
			 "public %1$s(MinecraftServer minecraftServer, WorldServer worldServer, GameProfile gameProfile, PlayerInteractManager playerInteractManager) {\n"
					 + "  super(minecraftServer, worldServer, gameProfile, playerInteractManager);\n"
					 + "  this.$npc = new %2$s(this);\n"
					 + "}",
			 "public %1$s(World world) {\n"
					 + "  super(((CraftServer) Bukkit.getServer()).getServer(), (WorldServer) world, new GameProfile(UUID.randomUUID(), \"NPCLib\"), new PlayerInteractManager((WorldServer) world));\n"
					 + "  this.$npc = new %2$s(this);\n"
					 + "}"
	 })
public class NPCPlayer extends NPCHumanAbstract<EntityPlayer, Player> {

	public NPCPlayer(EntityPlayer npcEntity) {
		super(npcEntity);
	}

	@Override
	protected void postInit(String pluginName, double x, double y, double z, float yaw, float pitch) throws Exception {
		System.out.println("Player-postInit: pluginName = [" + pluginName + "], x = [" + x + "], y = [" + y + "], z = [" + z + "], yaw = [" + yaw + "], pitch = [" + pitch + "]");

		if (this.pluginName != null) {
			this.getPlugin().getLogger().warning("[NPCLib] Attempt to change the NPCs plugin from " + this.pluginName + " to " + pluginName);
		} else {
			this.pluginName = pluginName;
		}

		// Create fake PlayerConnection
		new FieldResolver(Reflection.nmsClassResolver.resolve("EntityPlayer")).resolve("playerConnection").set(getNpcEntity(), ClassBuilder.buildPlayerConnection(ClassBuilder.buildNetworkManager(false), getNpcEntity()));
		// Initialize Gamemode
		getBukkitEntity().setGameMode(GameMode.SURVIVAL);

		for (Player player : Bukkit.getOnlinePlayers()) {
			updatePlayerList(player);
		}

		super.postInit(pluginName, x, y, z, yaw, pitch);
		//TODO: figure out a way to remove fake players from the player list without breaking everything
	}

	public GameProfileWrapper getProfile() {
		return new GameProfileWrapper(getNpcEntity().getProfile());
	}

	protected void setProfile(GameProfileWrapper profile) {
		try {
			UUID uuid = profile.getId();
			if (uuid == null) {
				uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + profile.getName()).getBytes(Charsets.UTF_8));
			}
			Method method = new MethodResolver(Reflection.nmsClassResolver.resolve("Entity")).resolve(new ResolverQuery("a", UUID.class));
			method.invoke(getNpcEntity(), uuid);

			Field field = new FieldResolver(Reflection.nmsClassResolver.resolve("EntityHuman")).resolveByFirstType(GameProfile.class);
			field.set(getNpcEntity(), profile.getHandle());
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public void setName(String name) {
		System.out.println(name);
		if (name.length() > 16) {
			throw new IllegalArgumentException("Maximum player name length is 16");
		}
		GameProfileWrapper profile = new GameProfileWrapper(getProfile().getId(), name);
		profile.getProperties().putAll(getProfile().getProperties());
		System.out.println(profile);
		setProfile(profile);
	}

	@Override
	public void readFromNBT(CompoundTag compoundTag) {
		super.readFromNBT(compoundTag);
		setName(compoundTag.getCompound("bukkit").getString("lastKnownName"));// TODO: might be more reliable to save the actual name when writing to NBT
		spawnPlayer();
	}

	protected void spawnPlayer() {
		NPCSpawnEvent event = new NPCSpawnEvent(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		Bukkit.getPluginManager().callEvent(event);
		getNpcEntity().spawnPlayer(event.getSpawnReason());
	}

	@Override
	public void onDie(SuperSwitch superSwitch) {
		System.out.println("onDie -> NPCPlayer");
		super.onDie(superSwitch);
		if (!superSwitch.isCancelled()) {
			if (getPlugin() == null || !getPlugin().isEnabled()) { return; }
			// Remove players from the world manually
			Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
				@Override
				public void run() {
					try {
						Reflection.nmsClassResolver.resolve("World").getDeclaredMethod("removeEntity").invoke(Minecraft.getHandle(getBukkitEntity().getWorld()), getNpcEntity());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}, 40);// <-- death animation delay
		}
	}

	protected void updatePlayerList(final Player player) {
		System.out.println("updatePlayerList(" + player + ")");
		System.out.println(getPlugin());
		if (getPlugin() == null || !getPlugin().isEnabled()) { return; }
		try {
			System.out.println(this);
			System.out.println("Update to " + player);
			System.out.println(getBukkitEntity());
			System.out.println(getBukkitEntity().getGameMode());
			System.out.println(getNpcEntity());
			sendPacket(player, ClassBuilder.buildPlayerInfoPacket(0, getNpcEntity().getProfile(), 0, getBukkitEntity().getGameMode().ordinal(), getBukkitEntity().getName()));
			new BukkitRunnable() {

				@Override
				public void run() {
					if (!player.isOnline()) { return; }
					if (/*TODO!NPCPlayerEntityBase.this.isShownInList()  ||*/ true || getBukkitEntity().isDead()) {
						try {
							sendPacket(player, ClassBuilder.buildPlayerInfoPacket(4, getNpcEntity().getProfile(), 0, getBukkitEntity().getGameMode().ordinal(), getBukkitEntity().getName()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.runTaskLater(getPlugin(), 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateToPlayer(final Player player) {
		this.updatePlayerList(player);
	}

}
