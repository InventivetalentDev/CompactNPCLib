package org.inventivetalent.npclib;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.npclib.command.SpawnCommands;
import org.inventivetalent.npclib.registry.NPCRegistry;
import org.inventivetalent.pluginannotations.PluginAnnotations;

public class NPCLibPlugin extends JavaPlugin implements Listener {

	private NPCRegistry pluginNpcRegistry;

	@Override
	public void onEnable() {
		NPCLib.logger = getLogger();
		Bukkit.getPluginManager().registerEvents(this, this);
		PluginAnnotations.COMMAND.load(this, new SpawnCommands(this));
		pluginNpcRegistry = NPCLib.createRegistry(this);
	}

	public NPCRegistry getPluginNpcRegistry() {
		return pluginNpcRegistry;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(CreatureSpawnEvent event) {
		System.out.println(event);
		System.out.println(event.isCancelled());
		event.setCancelled(false);
	}

}
