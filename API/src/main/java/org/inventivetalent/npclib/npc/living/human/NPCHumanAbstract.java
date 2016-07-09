package org.inventivetalent.npclib.npc.living.human;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.inventivetalent.nbt.annotation.NBT;
import org.inventivetalent.npclib.ClassBuilder;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.entity.living.human.NPCEntityHuman;
import org.inventivetalent.npclib.npc.living.NPCLivingAbstract;
import org.inventivetalent.npclib.skin.ISkinnableEntity;
import org.inventivetalent.npclib.skin.SkinLayer;
import org.inventivetalent.reflection.minecraft.DataWatcher;

public abstract class NPCHumanAbstract<N extends NPCEntityHuman, B extends HumanEntity> extends NPCLivingAbstract<N, B> implements ISkinnableEntity {

	@NBT(value = {
			"npclib.options",
			"human",
			"laying" },
		 read = false)
	private boolean laying;

	public NPCHumanAbstract(N npcEntity) {
		super(npcEntity);
	}

	@Override
	public void setSkinLayers(SkinLayer... visibleLayers) {
		setSkinLayerFlag(SkinLayer.getValue(visibleLayers));
	}

	@NBT({
				 "npclib.options",
				 "human",
				 "skinLayers" })
	protected void setSkinLayerFlag(@NBT int flag) {
		try {
			DataWatcher.setValue(Reflection.getDataWatcher(getBukkitEntity()), 10, DataWatcher.V1_9.ValueType.ENTITY_HUMAN_SKIN_LAYERS, (byte) flag);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@NBT({
				 "npclib.options",
				 "human",
				 "skinLayers" })
	protected int getSkinLayerFlag() {
		try {
			return (byte) DataWatcher.getValue(Reflection.getDataWatcher(getBukkitEntity()), 10, DataWatcher.V1_9.ValueType.ENTITY_HUMAN_SKIN_LAYERS);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@NBT(value = {
			"npclib.options",
			"human",
			"laying" })
	public void setLaying(@NBT boolean laying) {
		this.laying = laying;
		updateNearby();
	}

	public boolean isLaying() {
		return laying;
	}

	@Override
	public void updateToPlayer(Player player) {
		if (getBukkitEntity().isDead()) { return; }
		super.updateToPlayer(player);
		if (isLaying()) {
			Location bedLocation = getBukkitEntity().getLocation().clone();
			bedLocation.setY(0);
			// Create a fake bed block
			player.sendBlockChange(bedLocation, Material.BED_BLOCK, (byte) 0);

			sendPacket(player, ClassBuilder.buildPacketPlayOutBed(getBukkitEntity().getEntityId(), bedLocation.getBlockX(), bedLocation.getBlockY(), bedLocation.getBlockZ()));
		}
	}

}
