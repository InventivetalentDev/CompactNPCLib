package org.inventivetalent.npclib.entity;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.inventivetalent.npclib.annotation.ExtraMethod;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.watcher.MethodWatcher;
import org.inventivetalent.vectors.d3.Vector3DDouble;

public interface NPCEntity {

	void setMethodWatcher(MethodWatcher methodWatcher);

	NPCAbstract getNPC();

	//	<T extends Entity> T getBukkitEntity();

	boolean methodCalled(String name, Object[] args);

	Object methodCalled(String name, Object superValue, Object[] args);

	// Overwritten methods

	void setLocation(double x, double y, double z, float yaw, float pitch);

	void move(double x, double y, double z);

	// Helpers

	@ExtraMethod("public void spawn() {\n"
						 + "  this.world.addEntity(this);\n"
						 + "}")
	void spawn();

	@ExtraMethod("public void spawn(org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason spawnReason) {\n"
						 + "  this.world.addEntity(this, spawnReason);\n"
						 + "}")
	void spawn(CreatureSpawnEvent.SpawnReason spawnReason);

	@ExtraMethod("public org.inventivetalent.vectors.d3.Vector3DDouble getLocationVector() {\n"
						 + "  return new org.inventivetalent.vectors.d3.Vector3DDouble(this.locX, this.locY, this.locZ);\n"
						 + "}")
	Vector3DDouble getLocationVector();

	@ExtraMethod("public void setYaw(float yaw) {\n"
						 + "  this.yaw = yaw;\n"
						 + "}")
	void setYaw(float yaw);

	@ExtraMethod("public void setPitch(float pitch) {\n"
						 + "  this.pitch = pitch;\n"
						 + "}")
	void setPitch(float pitch);

}
