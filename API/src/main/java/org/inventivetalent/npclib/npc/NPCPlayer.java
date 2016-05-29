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

package org.inventivetalent.npclib.npc;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.mcwrapper.auth.GameProfileWrapper;
import org.inventivetalent.npclib.ClassBuilder;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.annotation.NPC;
import org.inventivetalent.npclib.entity.EntityPlayer;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.FieldResolver;

@NPC(id = -1,
	 type = EntityType.PLAYER,
	 bukkit = Player.class,
	 nms = "EntityPlayer",
	 entity = EntityPlayer.class,
	 extraPackages = {
			 "com.mojang.authlib"
	 },
	 constructors = {
			 "public %1$s(MinecraftServer minecraftServer, WorldServer worldServer, GameProfile gameProfile, PlayerInteractManager playerInteractManager) {\n"
					 + "  super(minecraftServer, worldServer, gameProfile, playerInteractManager);\n"
					 + "}"
	 })
public class NPCPlayer extends NPCAbstract<EntityPlayer, Player> {

	NPCPlayer(EntityPlayer npcEntity) {
		super(npcEntity);
	}

	@Override
	public void postInit(Location location) throws Exception {
		// Create fake PlayerConnection
		new FieldResolver(Reflection.nmsClassResolver.resolve("EntityPlayer")).resolve("playerConnection").set(getNpcEntity(), ClassBuilder.buildPlayerConnection(ClassBuilder.buildNetworkManager(false), getNpcEntity()));
		// Initialize Gamemode
		getBukkitEntity().setGameMode(GameMode.SURVIVAL);

		for (Player player : Bukkit.getOnlinePlayers()) {
			updatePlayerList(player);
		}

		super.postInit(location);
		//TODO: figure out a way to remove fake players from the player list without breaking everything
	}

	public GameProfileWrapper getProfile() {
		return new GameProfileWrapper(getNpcEntity().getProfile());
	}

	@Override
	public boolean onDie() {
		System.out.println("onDie -> NPCPlayer");
		boolean die = super.onDie();
		if (die) {
			// Remove players from the world manually
			Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("NPCLib"), new Runnable() {
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
		return die;
	}

	void updatePlayerList(final Player player) {
		try {
			System.out.println(this);
			System.out.println("Update to " + player);
			System.out.println(getBukkitEntity());
			System.out.println(getBukkitEntity().getGameMode());
			System.out.println(getNpcEntity());
			this.sendPacket(player, ClassBuilder.buildPlayerInfoPacket(0, getNpcEntity().getProfile(), 0, getBukkitEntity().getGameMode().ordinal(), getBukkitEntity().getName()));
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
			}.runTaskLater(Bukkit.getPluginManager().getPlugin("NPCLib"), 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
