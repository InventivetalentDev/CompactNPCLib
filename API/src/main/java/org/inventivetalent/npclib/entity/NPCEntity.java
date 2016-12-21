package org.inventivetalent.npclib.entity;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.inventivetalent.npclib.annotation.ExtraMethod;
import org.inventivetalent.npclib.annotation.NPCInfo;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.watcher.MethodWatcher;
import org.inventivetalent.vectors.d3.Vector3DDouble;

public interface NPCEntity {

	void setMethodWatcher(MethodWatcher methodWatcher);

	@ExtraMethod("public NPCAbstract getNPC() {\n"
						 + "  return this.$npc;\n"
						 + "}")
	NPCAbstract<?, ?> getNPC();

	//	<T extends Entity> T getBukkitEntity();

	boolean methodCalled(String name, Object[] args);

	Object methodCalled(String name, Object superValue, Object[] args);

	NPCInfo getNpcInfo();

	void setNpcInfo(NPCInfo npcInfo);

	// Overwritten methods

	void setLocation(double x, double y, double z, float yaw, float pitch);

	// The EnumMoveType was added in 1.11 as an anticheat feature.
	@ExtraMethod("public void move(double x, double y, double z) {\n"
			+ "  move(EnumMoveType.SELF, x, y, z);\n"
			+ "}")
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

	// mot? accessors

	@ExtraMethod("public double getMotX() {"
						 + "  return this.motX;\n"
						 + "}")
	double getMotX();

	@ExtraMethod("public void setMotX(double motX) {\n"
						 + "  this.motX = motX;\n"
						 + "}")
	void setMotX(double motX);

	@ExtraMethod("public double getMotY() {"
						 + "  return this.motY;\n"
						 + "}")
	double getMotY();

	@ExtraMethod("public void setMotY(double motY) {\n"
						 + "  this.motY = motY;\n"
						 + "}")
	void setMotY(double motY);

	@ExtraMethod("public double getMotZ() {"
						 + "  return this.motZ;\n"
						 + "}")
	double getMotZ();

	@ExtraMethod("public void setMotZ(double motZ) {\n"
						 + "  this.motZ = motZ;\n"
						 + "}")
	void setMotZ(double motZ);

}
