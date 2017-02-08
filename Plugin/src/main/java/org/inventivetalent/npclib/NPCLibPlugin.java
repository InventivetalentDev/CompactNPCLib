package org.inventivetalent.npclib;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.apihelper.APIManager;
import org.inventivetalent.npclib.command.SpawnCommands;
import org.inventivetalent.npclib.metrics.Metrics;
import org.inventivetalent.npclib.registry.NPCRegistry;
import org.inventivetalent.pluginannotations.PluginAnnotations;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

public class NPCLibPlugin extends JavaPlugin implements Listener {

	private NPCLib npcLibInstance = new NPCLib();
	private NPCRegistry pluginNpcRegistry;

	@Override
	public void onLoad() {
		APIManager.registerAPI(npcLibInstance, this);
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();

		NPCLib.logger = getLogger();
		NPCLib.debug = getConfig().getBoolean("debug");

		APIManager.initAPI(NPCLib.class);
		pluginNpcRegistry = NPCLib.createRegistry(this);

		Bukkit.getPluginManager().registerEvents(this, this);
		PluginAnnotations.COMMAND.load(this, new SpawnCommands(this));

		new Metrics(this);

		SpigetUpdate spigetUpdate = new SpigetUpdate(this, 5853).setUserAgent("NPCLib/" + getDescription().getVersion()).setVersionComparator(VersionComparator.SEM_VER);
		spigetUpdate.checkForUpdate(new UpdateCallback() {
			@Override
			public void updateAvailable(String s, String s1, boolean b) {
				getLogger().info("A new version is available (" + s + "). Download it from https://r.spiget.org/5853");
			}

			@Override
			public void upToDate() {
				getLogger().info("The plugin is up-to-date.");
			}
		});
	}

	@Override
	public void onDisable() {
		APIManager.disableAPI(NPCLib.class);
	}

	public NPCRegistry getPluginNpcRegistry() {
		return pluginNpcRegistry;
	}

}