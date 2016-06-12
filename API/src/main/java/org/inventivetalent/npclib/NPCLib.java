package org.inventivetalent.npclib;

import com.mojang.authlib.GameProfile;
import lombok.NonNull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.inventivetalent.apihelper.API;
import org.inventivetalent.npclib.entity.NPCEntity;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.npc.living.human.NPCPlayer;
import org.inventivetalent.npclib.registry.NPCRegistry;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.PacketOptions;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;
import org.inventivetalent.reflection.minecraft.Minecraft;

import java.util.UUID;
import java.util.logging.Logger;

public class NPCLib implements API {

	public static Logger logger = Logger.getLogger("NPCLib");

	public static NPCRegistry createRegistry(@NonNull Plugin plugin) {
		//		return Reflection.newInstance("org.inventivetalent.npc.registry", "NPCRegistry", NPCRegistry.class, plugin);
		return new NPCRegistry(plugin);
	}

	public static boolean isNPC(Entity entity) {
		if (entity == null) { return false; }
		try {
			return Minecraft.getHandle(entity) instanceof NPCEntity;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static NPCAbstract getNPC(Entity entity) {
		if (entity == null) { return null; }
		try {
			Object handle = Minecraft.getHandle(entity);
			if (handle instanceof NPCEntity) {
				return ((NPCEntity) handle).getNPC();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public void load() {
	}

	@Override
	public void init(final Plugin plugin) {
		PacketHandler.addHandler(new PacketHandler(plugin) {
			@PacketOptions(forcePlayer = true)
			@Override
			public void onSend(final SentPacket sentPacket) {
				if (sentPacket.hasPlayer()) {
					if ("PacketPlayOutNamedEntitySpawn".equals(sentPacket.getPacketName())) {
						final Player player = sentPacket.getPlayer();
						final UUID uuid = Minecraft.VERSION.newerThan(Minecraft.Version.v1_8_R1) ?
								((UUID) sentPacket.getPacketValue("b")) :
								(((GameProfile) sentPacket.getPacketValue("b")).getId());
						Player npcPlayer = null;
						//TODO: check if this doesn't cause any ConcurrentModExceptions / make this synchronous somehow
						for (Player worldPlayer : player.getWorld().getPlayers()) {// We can't use Bukkit#getOnlinePlayers, since the server doesn't know about the player NPCs
							if (worldPlayer.getUniqueId().equals(uuid)) {
								npcPlayer = worldPlayer;
								break;
							}
						}
						if (npcPlayer != null) {
							NPCAbstract npcAbstract = NPCLib.getNPC(npcPlayer);
							if (npcAbstract != null && npcAbstract instanceof NPCPlayer) {
								((NPCPlayer) npcAbstract).updateToPlayer(player);
							}
						}
					}
				}
			}

			@Override
			public void onReceive(ReceivedPacket receivedPacket) {
			}
		});
	}

	@Override
	public void disable(Plugin plugin) {
	}
}
