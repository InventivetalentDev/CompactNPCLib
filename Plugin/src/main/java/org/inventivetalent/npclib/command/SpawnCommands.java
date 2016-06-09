package org.inventivetalent.npclib.command;

import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.inventivetalent.npclib.NPCLibPlugin;
import org.inventivetalent.npclib.NPCType;
import org.inventivetalent.pluginannotations.command.*;

import java.util.List;

public class SpawnCommands {

	private NPCLibPlugin plugin;

	public SpawnCommands(NPCLibPlugin plugin) {
		this.plugin = plugin;
	}

	@Command(name = "spawnNpc",
			 aliases = {},
			 usage = "<Type> [x] [y] [z] [world]",
			 description = "Spawn an NPC at your location or the specified coordinates",
			 min = 1,
			 max = 5,
			 fallbackPrefix = "npclib",
			 errorHandler = FeedbackErrorHandler.class)
	@Permission("npclib.command.spawn")
	public void spawnNPC(CommandSender sender, String typeString, @OptionalArg Double x, @OptionalArg Double y, @OptionalArg Double z, @OptionalArg String worldName) {
		NPCType npcType = NPCType.fromString(typeString);
		if (npcType == null) {
			sender.sendMessage("§cCould not find type for '" + typeString + "'");
			return;
		}
		Location location;
		if (sender instanceof Entity) {
			location = ((Entity) sender).getLocation();
			if (x != null) { location.setX(x); }
			if (y != null) { location.setY(y); }
			if (z != null) { location.setZ(z); }
			if (worldName != null) {
				World world = Bukkit.getWorld(worldName);
				if (world == null) {
					sender.sendMessage("§cWorld '" + worldName + "' does not exist");
					return;
				}
				location.setWorld(world);
			}
		} else {
			if (Strings.isNullOrEmpty(worldName)) {
				sender.sendMessage("§cPlease specify the location");
				return;
			}
			World world = Bukkit.getWorld(worldName);
			if (world == null) {
				sender.sendMessage("§cWorld '" + worldName + "' does not exist");
				return;
			}
			location = new Location(world, x, y, z);
		}
		plugin.getPluginNpcRegistry().createNPC(location, npcType);
		sender.sendMessage("§aNPC spawned at §7" + location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ());
	}

	@Completion(name = "spawnNpc")
	public void spawnNPC(List<String> list, CommandSender sender, String typeString, @OptionalArg Double x, @OptionalArg Double y, @OptionalArg Double z, @OptionalArg String worldName) {
		if (Strings.isNullOrEmpty(typeString)) {
			for (NPCType npcType : NPCType.values()) {
				list.add(npcType.name());
			}
			return;
		}
		if (x != null && y != null && z != null) {
			if (Strings.isNullOrEmpty(worldName)) {
				for (World world : Bukkit.getWorlds()) {
					list.add(world.getName());
				}
			}
		}
	}

}
