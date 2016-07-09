package org.inventivetalent.npclib.test;

import org.inventivetalent.npclib.skin.SkinLayer;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SkinLayerTest {

	@Test
	public void skinLayerValueTest() {
		int value = SkinLayer.getValue(new SkinLayer[] {
				SkinLayer.CAPE,
				SkinLayer.LEFT_SLEEVE,
				SkinLayer.RIGHT_PANTS_LEG });
		assertEquals(37, value);

		List<SkinLayer> layers = (List<SkinLayer>) SkinLayer.getLayers(value);
		assertTrue(layers.contains(SkinLayer.CAPE));
		assertTrue(layers.contains(SkinLayer.LEFT_SLEEVE));
		assertTrue(layers.contains(SkinLayer.RIGHT_PANTS_LEG));
	}

}
