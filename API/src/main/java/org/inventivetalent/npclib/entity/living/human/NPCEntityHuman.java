package org.inventivetalent.npclib.entity.living.human;

import com.mojang.authlib.GameProfile;
import org.inventivetalent.npclib.entity.living.NPCEntityLiving;

public interface NPCEntityHuman extends NPCEntityLiving {

	GameProfile getProfile();

	void checkMovement(double x, double y, double z);

}
