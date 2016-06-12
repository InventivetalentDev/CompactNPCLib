package org.inventivetalent.npclib;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.inventivetalent.apihelper.API;
import org.inventivetalent.npclib.entity.NPCEntity;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.registry.NPCRegistry;
import org.inventivetalent.reflection.minecraft.Minecraft;

import java.util.logging.Logger;

public class NPCLib implements API {

	public static Logger logger = Logger.getLogger("NPCLib");

	public static NPCRegistry createRegistry(Plugin plugin) {
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
	public void init(Plugin plugin) {
	}

	@Override
	public void disable(Plugin plugin) {
	}
}
