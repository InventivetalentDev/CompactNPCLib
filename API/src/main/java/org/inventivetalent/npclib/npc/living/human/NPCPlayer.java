package org.inventivetalent.npclib.npc.living.human;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.inventivetalent.mcwrapper.auth.GameProfileWrapper;
import org.inventivetalent.mcwrapper.auth.properties.PropertyWrapper;
import org.inventivetalent.nbt.CompoundTag;
import org.inventivetalent.nbt.TagID;
import org.inventivetalent.nbt.annotation.NBT;
import org.inventivetalent.nicknamer.api.SkinLoader;
import org.inventivetalent.npclib.ClassBuilder;
import org.inventivetalent.npclib.ObjectContainer;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.SuperSwitch;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.living.human.EntityPlayer;
import org.inventivetalent.npclib.event.NPCDeathEvent;
import org.inventivetalent.npclib.event.NPCSpawnEvent;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.ResolverQuery;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
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

	@NBT(value = {
			"npclib.options",
			"player",
			"skinTexture",
			"value" },
		 type = TagID.TAG_STRING,
		 read = false)
	private String  skinTextureValue;
	@NBT(value = {
			"npclib.options",
			"player",
			"skinTexture",
			"signature" },
		 type = TagID.TAG_STRING,
		 read = false)
	private String  skinTextureSignature;
	@NBT(value = {
			"npclib.options",
			"player",
			"showInList" },
		 type = TagID.TAG_BYTE)
	private boolean showInList;
	@NBT({
				 "npclib.options",
				 "player",
				 "nameHidden" })
	private boolean nameHidden;
	@NBT({
				 "npclib.options",
				 "player",
				 "scoreboard",
				 "prefix" })
	private String  scoreboardPrefix;
	@NBT({
				 "npclib.options",
				 "player",
				 "scoreboard",
				 "suffix" })
	private String  scoreboardSuffix;

	public NPCPlayer(EntityPlayer npcEntity) {
		super(npcEntity);
	}

	@Override
	protected void postInit(String pluginName, double x, double y, double z, float yaw, float pitch) throws Exception {
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

	protected Team getScoreboardTeam() {
		String name = getBukkitEntity().getUniqueId().toString().replace("-", "").substring(0, 16);
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		Team team = scoreboard.getTeam(name);
		if (team == null) {
			team = scoreboard.registerNewTeam(name);
		}
		return team;
	}

	public void refreshScoreboard() {
		Team team = getScoreboardTeam();

		for (String entry : team.getEntries()) {
			team.removeEntry(entry);
		}
		team.addEntry(getBukkitEntity().getName());

		team.setPrefix(Strings.nullToEmpty(scoreboardPrefix));
		team.setSuffix(Strings.nullToEmpty(scoreboardSuffix));

		if (nameHidden) {
			team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		} else {
			team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
		}
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
			if (Minecraft.VERSION.newerThan(Minecraft.Version.v1_9_R1)) {
				Method method = new MethodResolver(Reflection.nmsClassResolver.resolve("Entity")).resolve(new ResolverQuery("a", UUID.class));
				method.invoke(getNpcEntity(), uuid);
			}

			Field field = new FieldResolver(Reflection.nmsClassResolver.resolve("EntityHuman")).resolveByFirstType(GameProfile.class);
			field.set(getNpcEntity(), profile.getHandle());
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
		refreshScoreboard();
	}

	public void setName(String name) {
		if (name.length() > 16) {
			throw new IllegalArgumentException("Maximum player name length is 16");
		}
		GameProfileWrapper profile = new GameProfileWrapper(getProfile().getId(), name);
		profile.getProperties().putAll(getProfile().getProperties());
		setProfile(profile);
	}

	public void setNamePrefix(String prefix) {
		if (prefix.length() > 16) {
			throw new IllegalArgumentException("Maximum prefix length is 16");
		}
		this.scoreboardPrefix = prefix;
		refreshScoreboard();
	}

	public void setNameSuffix(String suffix) {
		if (suffix.length() > 16) {
			throw new IllegalArgumentException("Maximum suffix length is 16");
		}
		this.scoreboardSuffix = suffix;
		refreshScoreboard();
	}

	public void setFullName(String fullName) {
		if (fullName.length() > 48) {
			throw new IllegalArgumentException("Maximum full name length is 48 (16 + 16 + 16)");
		}
		if (fullName.length() < 16) {// Name is short enough to work without prefix/suffix
			this.scoreboardPrefix = null;
			this.scoreboardSuffix = null;
			setName(fullName);
		} else if (fullName.length() < 32) {// Only needs prefix
			this.scoreboardSuffix = null;
			this.scoreboardPrefix = optionalSubstring(fullName, 0, 16);
			setName(optionalSubstring(fullName, 16, 32));
		} else {
			this.scoreboardPrefix = optionalSubstring(fullName, 0, 16);
			this.scoreboardSuffix = optionalSubstring(fullName, 32, 48);
			setName(optionalSubstring(fullName, 16, 32));
		}
	}

	public String getFullName() {
		return Strings.nullToEmpty(this.scoreboardPrefix) + getProfile().getName() + Strings.nullToEmpty(this.scoreboardSuffix);
	}

	String optionalSubstring(String string, int start, int end) {
		return StringUtils.substring(string, start, end);
	}

	@Override
	public void setNameVisible(boolean visible) {
		super.setNameVisible(visible);
		this.nameHidden = !visible;
		refreshScoreboard();
	}

	@Override
	public boolean isNameVisible() {
		return !this.nameHidden;
	}

	@NBT({
				 "npclib.options",
				 "player",
				 "skinTexture" })
	@Override
	public void setSkinTexture(@NBT(value = "value",
									type = TagID.TAG_STRING) String value, @NBT(value = "signature",
																				type = TagID.TAG_STRING) String signature) {
		this.skinTextureValue = value;
		this.skinTextureSignature = signature;

		GameProfileWrapper profile = getProfile();
		profile.getProperties().clear();
		profile.getProperties().put("textures", new PropertyWrapper("textures", value, signature));
		setProfile(profile);

		updateNearby();
		if (getPlugin() != null && getPlugin().isEnabled()) {
			// Give it some time to load the profile
			Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
				@Override
				public void run() {
					updateNearby(32, new Predicate<Player>() {
						@Override
						public boolean apply(@Nullable Player player) {
							respawnTo(player);
							return true;
						}
					});
				}
			}, 20);
		}
	}

	public void setSkin(final String skinOwner) {
		if (Bukkit.getPluginManager().isPluginEnabled("NickNamer")) {
			if (getPlugin() != null && getPlugin().isEnabled()) {
				Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable() {
					@Override
					public void run() {
						GameProfileWrapper profile = SkinLoader.loadSkin(skinOwner);
						Collection<PropertyWrapper> values = profile.getProperties().values();
						if (values.isEmpty()) { return; }
						PropertyWrapper property = values.iterator().next();
						setSkinTexture(property.getValue(), property.getSignature());
					}
				});
			}
		}
	}

	public void setShowInList(boolean showInList) {
		this.showInList = showInList;
	}

	public boolean isShownInList() {
		return showInList;
	}

	@Override
	public void writeToNBT(CompoundTag compoundTag) {
		super.writeToNBT(compoundTag);
		CompoundTag playerTag = compoundTag.getOrCreateCompound("npclib.player");
		playerTag.set("name", getProfile().getName());
		playerTag.set("uuid", getProfile().getId().toString());

		//		if (this.skinTextureValue != null) {
		//			CompoundTag skinTextureTag = playerTag.getOrCreateCompound("skinTexture");
		//			skinTextureTag.set("value", this.skinTextureValue);
		//			skinTextureTag.set("signature", this.skinTextureSignature);
		//		}

		//		CompoundTag optionsTag = compoundTag.getOrCreateCompound("npclib.options");
		//		CompoundTag playerOptions = optionsTag.getOrCreateCompound("player");
		//		playerOptions.set("shownInList", isShownInList());
	}

	@Override
	public void readFromNBT(CompoundTag compoundTag) {
		CompoundTag playerTag = compoundTag.getCompound("npclib.player");
		if (playerTag != null) {
			setProfile(new GameProfileWrapper(UUID.fromString(playerTag.getString("uuid")), playerTag.getString("name")));

			//			CompoundTag skinTextureTag = playerTag.getCompound("skinTexture");
			//			if (skinTextureTag != null) {
			//				setSkinTexture(skinTextureTag.getString("value"), skinTextureTag.getString("signature"));
			//			}
		}
		super.readFromNBT(compoundTag);

		//		CompoundTag optionsTag = compoundTag.getCompound("npclib.options");
		//		if (optionsTag != null) {
		//			CompoundTag playerOptions = optionsTag.getCompound("player");
		//			if (playerOptions != null) {
		//				setShowInList(playerOptions.getBoolean("shownInList"));
		//			}
		//		}

		spawnPlayer();
	}

	protected void spawnPlayer() {
		NPCSpawnEvent event = new NPCSpawnEvent(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
		Bukkit.getPluginManager().callEvent(event);
		getNpcEntity().spawnPlayer(event.getSpawnReason());
	}

	@Override
	public void despawn() {
		NPCDeathEvent event = new NPCDeathEvent(this, null, null);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) { return; }
		getBukkitEntity().remove();
		removeOnDeath(new SuperSwitch(SuperSwitch.State.PASS)/*Dummy*/);
	}

	@Override
	public void onDie(SuperSwitch superSwitch) {
		super.onDie(superSwitch);
		removeOnDeath(superSwitch);
	}

	@Override
	public void onDie(ObjectContainer<Object> damageSource, SuperSwitch superSwitch) {
		super.onDie(damageSource, superSwitch);
		removeOnDeath(superSwitch);
	}

	protected void removeOnDeath(SuperSwitch superSwitch) {
		if (!superSwitch.isCancelled()) {
			if (getBukkitEntity().isDead()) { return; }
			if (getPlugin() == null || !getPlugin().isEnabled()) { return; }
			// Remove players from the world manually
			Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
				@Override
				public void run() {
					try {
						Reflection.nmsClassResolver.resolve("World").getDeclaredMethod("removeEntity", Reflection.nmsClassResolver.resolve("Entity")).invoke(Minecraft.getHandle(getBukkitEntity().getWorld()), getNpcEntity());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}, 40);// <-- death animation delay
		}
	}

	protected void updatePlayerList(final Player player) {
		if (getPlugin() == null || !getPlugin().isEnabled()) { return; }
		try {
			sendPacket(player, ClassBuilder.buildPlayerInfoPacket(0, getNpcEntity().getProfile(), 0, getBukkitEntity().getGameMode().ordinal(), getBukkitEntity().getName()));
			new BukkitRunnable() {

				@Override
				public void run() {
					if (!player.isOnline()) { return; }
					if (!isShownInList() || getBukkitEntity().isDead()) {
						try {
							sendPacket(player, ClassBuilder.buildPlayerInfoPacket(4, getNpcEntity().getProfile(), 0, getBukkitEntity().getGameMode().ordinal(), getBukkitEntity().getName()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}.runTaskLater(getPlugin(), 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateToPlayer(final Player player) {
		if (getBukkitEntity().isDead()) { return; }
		super.updateToPlayer(player);
		this.updatePlayerList(player);
	}

	public void updateNearby(double radius, Predicate<Player> predicate) {
		double radiusSquared = radius * radius;
		for (Player player : getBukkitEntity().getWorld().getPlayers()) {
			if (player.getLocation().distanceSquared(getBukkitEntity().getLocation()) < radiusSquared) {
				if (predicate == null || predicate.apply(player)) {
					updateToPlayer(player);
				}
			}
		}
	}

	public void updateNearby(double radius) {
		updateNearby(radius, null);
	}

	public void updateNearby() {
		updateNearby(32);
	}
}
