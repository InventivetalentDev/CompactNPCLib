package org.inventivetalent.npclib.animation;

public enum Animation {

	SWING_MAIN_ARM(Type.ANIMATION, 0),
	TAKE_DAMAGE(Type.ANIMATION, 1),
	LEAVE_BED(Type.ANIMATION, 2),
	SWING_OFF_HAND(Type.ANIMATION, 3),
	CRITICAL_EFFECT(Type.ANIMATION, 4),
	CRITICAL_EFFECT_MAGIC(Type.ANIMATION, 5),

	ENTITY_HURT(Type.STATUS, 2),
	ENTITY_DEATH(Type.STATUS, 3),
	IRON_GOLEM_ARMS(Type.STATUS, 4),
	WOLF_SHAKE(Type.STATUS, 8),
	SHEEP_EAT(Type.STATUS, 10),
	IRON_GOLEM_ROSE(Type.STATUS, 11),
	RESET_SQUID_ROTATION(Type.STATUS, 19);

	private final Type type;
	private final int  id;

	Animation(Type type, int id) {
		this.type = type;
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public enum Type {
		STATUS,
		ANIMATION;
	}

}
