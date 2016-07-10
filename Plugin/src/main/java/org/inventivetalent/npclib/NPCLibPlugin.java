package org.inventivetalent.npclib;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.apihelper.APIManager;
import org.inventivetalent.npclib.command.SpawnCommands;
import org.inventivetalent.npclib.registry.NPCRegistry;
import org.inventivetalent.pluginannotations.PluginAnnotations;

public class NPCLibPlugin extends JavaPlugin implements Listener {

	private NPCLib npcLibInstance = new NPCLib();
	private NPCRegistry pluginNpcRegistry;

	@Override
	public void onLoad() {
		APIManager.registerAPI(npcLibInstance, this);
	}

	@Override
	public void onEnable() {
		if (!Bukkit.getPluginManager().isPluginEnabled("PacketListenerAPI")) {
			getLogger().severe("Please install PacketListenerAPI: http://www.spigotmc.org/resources/2930/");
			throw new RuntimeException("PacketListenerAPI not installed");
		}
		if (!Bukkit.getPluginManager().isPluginEnabled("NickNamer")) {
			getLogger().warning("Please install NickNamer for proper skin support: http://www.spigotmc.org/resources/5341/");
		}

		saveDefaultConfig();

		NPCLib.logger = getLogger();
		NPCLib.debug = getConfig().getBoolean("debug");

		APIManager.initAPI(NPCLib.class);
		pluginNpcRegistry = NPCLib.createRegistry(this);

		Bukkit.getPluginManager().registerEvents(this, this);
		PluginAnnotations.COMMAND.load(this, new SpawnCommands(this));
	}

	@Override
	public void onDisable() {
		APIManager.disableAPI(NPCLib.class);
	}

	public NPCRegistry getPluginNpcRegistry() {
		return pluginNpcRegistry;
	}

}