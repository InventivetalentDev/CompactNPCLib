package org.inventivetalent.npclib.entity.living.human;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.inventivetalent.npclib.annotation.ExtraMethod;

public interface EntityPlayer extends NPCEntityHuman {

	@ExtraMethod("public void spawnPlayer(org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason spawnReason) {\n"
						 + "  this.world.players.add(this);\n"
						 + "  this.world.entityList.add(this);\n"
						 + "}")
	void spawnPlayer(CreatureSpawnEvent.SpawnReason spawnReason);
}
