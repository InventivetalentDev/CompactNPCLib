package org.inventivetalent.npclib.skin;

public enum SkinLayer {

	CAPE(0),
	JACKET(1),
	LEFT_SLEEVE(2),
	RIGHT_SLEEVE(3),
	LEFT_PANTS_LEG(4),
	RIGHT_PANTS_LEG(5),
	HAT(6);

	private final int id;
	private final int shifted;

	private SkinLayer(int id) {
		this.id = id;
		this.shifted = 1 << id;
	}

	public int getID() {
		return this.id;
	}

	public int getShifted() {
		return this.shifted;
	}

}
