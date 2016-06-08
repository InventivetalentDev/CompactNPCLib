package org.inventivetalent.npclib.test;

import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.NPCType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeTest {

	@Test
	public void entityTypeTest() {
		assertEquals(NPCType.ARMOR_STAND, NPCType.forEntityType(EntityType.ARMOR_STAND));
		assertEquals(NPCType.HORSE, NPCType.forEntityType(EntityType.HORSE));
	}

	@Test
	public void fromStringTest() {
		assertEquals(NPCType.CHICKEN, NPCType.fromString("chicken"));
		assertEquals(NPCType.MUSHROOM_COW, NPCType.fromString("mushroom cow"));
		assertEquals(NPCType.MUSHROOM_COW, NPCType.fromString("mushroomCow"));
		assertEquals(NPCType.SHULKER, NPCType.fromString("Shulker"));
		assertEquals(NPCType.ENDER_DRAGON, NPCType.fromString("EnderDragon"));
	}

}
