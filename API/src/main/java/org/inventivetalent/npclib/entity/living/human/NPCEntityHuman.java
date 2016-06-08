package org.inventivetalent.npclib.entity.living.human;

import org.inventivetalent.npclib.entity.living.NPCEntityLiving;

public interface NPCEntityHuman extends NPCEntityLiving {
	void checkMovement(double x, double y, double z);
}
