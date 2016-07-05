package org.inventivetalent.npclib.skin;

public interface ISkinnableEntity {

	void setSkinLayers(SkinLayer... visibleLayers);

	void setSkinTexture(String value, String signature);

}
