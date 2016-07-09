package org.inventivetalent.npclib.skin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum SkinLayer {

	CAPE(0),
	JACKET(1),
	LEFT_SLEEVE(2),
	RIGHT_SLEEVE(3),
	LEFT_PANTS_LEG(4),
	RIGHT_PANTS_LEG(5),
	HAT(6);

	public static final SkinLayer[] ALL = new SkinLayer[] {
			CAPE,
			JACKET,
			LEFT_SLEEVE,
			RIGHT_SLEEVE,
			LEFT_PANTS_LEG,
			RIGHT_PANTS_LEG,
			HAT };

	private final int id;
	private final int shifted;

	SkinLayer(int id) {
		this.id = id;
		this.shifted = 1 << id;
	}

	public int getID() {
		return this.id;
	}

	public int getShifted() {
		return this.shifted;
	}

	public boolean isActive(int value) {
		return (value & getShifted()) == getShifted();
	}

	public static int getValue(Iterable<SkinLayer> active) {
		int i = 0;
		for (SkinLayer layer : active) {
			i |= layer.getShifted();
		}
		return i;
	}

	public static int getValue(SkinLayer[] active) {
		int i = 0;
		for (SkinLayer layer : active) {
			i |= layer.getShifted();
		}
		return i;
	}

	public static Collection<SkinLayer> getLayers(int value) {
		List<SkinLayer> layers = new ArrayList<>();
		for (SkinLayer layer : values()) {
			if (layer.isActive(value)) { layers.add(layer); }
		}
		return layers;
	}

}
