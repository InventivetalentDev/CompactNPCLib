package org.inventivetalent.npclib.command;

import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.inventivetalent.npclib.NPCLibPlugin;
import org.inventivetalent.npclib.NPCType;
import org.inventivetalent.npclib.npc.living.human.NPCPlayer;
import org.inventivetalent.pluginannotations.command.*;

import java.util.List;
import java.util.UUID;

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
		Location location = parseLocation(sender, x, y, z, worldName);
		if (location == null) { return; }

		plugin.getPluginNpcRegistry().spawnNPC(location, npcType);
		sender.sendMessage("§aNPC spawned at §7" + location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ());
	}

	@Command(name = "spawnPlayer",
			 aliases = { "spawnNpcPlayer" },
			 usage = "<Name> [x] [y] [z] [world]",
			 description = "Spawn a Player NPC at your location or the specified coordinates",
			 min = 1,
			 max = 5,
			 fallbackPrefix = "npclib",
			 errorHandler = FeedbackErrorHandler.class)
	@Permission("npclib.command.spawnplayer")
	public void spawnPlayer(CommandSender sender, String name, @OptionalArg Double x, @OptionalArg Double y, @OptionalArg Double z, @OptionalArg String worldName) {
		if (name.length() > 16) {
			sender.sendMessage("§cName is too long (" + name.length() + ">16)");
			return;
		}
		Location location = parseLocation(sender, x, y, z, worldName);
		if (location == null) { return; }

		NPCPlayer npc = plugin.getPluginNpcRegistry().spawnPlayerNPC(location, NPCPlayer.class, UUID.randomUUID(), name);
		//TODO: remove, inventivetalent's skin
		npc.setSkin("md_5");
//		npc.setSkinTexture(
//				"eyJ0aW1lc3RhbXAiOjE0NjM4NDExMzM4NTEsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJJbnZlbnRpdmVHYW1lcyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDljMjU0NjFiZDgxMWM5NTg2ZDgyMThkOWZmNDUyYTFmNTZjNTA0OTI4MmRmYWIwMjBjNWZjZTMxYjQ0NzMifX19",
//				"hB7tRhUXvynDGt5zD7ONH65AXfpDZ0msg4VuIiFG4mbDA70f6vMo6YZ4roHQMNPXVZRNrRZzbejvmN+2qtDTKlovZlLNKFKyeRFOJjDA4mGZWMwneYQ62UmeA2aNfqog01gQGpz+VYWwtgx1cJaBmSKDsAK4MT9kfovdqAjk9oEXyx+DtatFEOZNiDN68gem8pP8R7WUP8YOFRqi/fLfVn7rH+OuZHxqczyri03TedkDTYnrIhZg5ksBtUK2ajQlZ9kcKv9SCPA82vzYnTRMW7+eOJ9ko9BlqktYm9Otj5KlqzjttLH9fjYHDtPyfPDDpLUhm6xN1zQrLpb2GRM+J3nvUlYCOURrg0ydxmyie0X/CGOJn+AxpYs2C54Kvl6rV2RG0K6VtbrEBjL2Wi+IZX9/66k+/EByyp3+geTu2E83paF0I1qH8og+x7g4ZYjBJQ4mAhHjBpvx6iNn80ofz8Yz8ZLaDWgdF50I6D1YcLoXkzRmVnu7ar/xzhsj2s2NLoY9mquBGs/uijU6sIJA1AN4j8eQB2XzWaW84bf13SUKJTQbpSi9VHZfIhz/HuguHCES52OkkDlYgC9no1P48jzDnws57dI/B6AnBJ/SDfV7CO7ILJ9egUEycOT8hkZvOzgharSnHQ8x05lxaaSkx1P8GDT621MPftkv9gM2+3s=");
		sender.sendMessage("§aNPC spawned at §7" + location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ());
	}

	@Completion(name = "spawnNpc")
	public void spawnNPC(List<String> list, CommandSender sender, String typeString, @OptionalArg Double x, @OptionalArg Double y, @OptionalArg Double z, @OptionalArg String worldName) {
		if (x == null && NPCType.fromString(typeString) == null) {
			for (NPCType npcType : NPCType.values()) {
				list.add(npcType.name());
			}
			return;
		}
		if (x != null && y != null && z != null) {
			for (World world : Bukkit.getWorlds()) {
				list.add(world.getName());
			}
		}
	}

	@Completion(name = "spawnPlayer")
	public void spawnPlayer(List<String> list, CommandSender sender, String name, @OptionalArg Double x, @OptionalArg Double y, @OptionalArg Double z, @OptionalArg String worldName) {
		if (name != null && x != null && y != null && z != null) {
			for (World world : Bukkit.getWorlds()) {
				list.add(world.getName());
			}
		}
	}

	Location parseLocation(CommandSender sender, Double x, Double y, Double z, String worldName) {
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
					return null;
				}
				location.setWorld(world);
			}
		} else {
			if (Strings.isNullOrEmpty(worldName)) {
				sender.sendMessage("§cPlease specify the location");
				return null;
			}
			World world = Bukkit.getWorld(worldName);
			if (world == null) {
				sender.sendMessage("§cWorld '" + worldName + "' does not exist");
				return null;
			}
			location = new Location(world, x, y, z);
		}
		return location;
	}

}
