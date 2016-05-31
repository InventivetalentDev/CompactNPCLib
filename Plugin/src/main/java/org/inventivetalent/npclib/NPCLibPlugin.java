/*
 * Copyright 2016 inventivetalent.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
 */

package org.inventivetalent.npclib;

import net.minecraft.server.v1_9_R1.Entity;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.npc.living.creature.monster.NPCCaveSpider;

public class NPCLibPlugin extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		NPCLib.logger = getLogger();
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (!event.getPlayer().isSneaking()) { return; }

//		NPCPlayer entity = NPCLib.createRegistry(this).createPlayerNPC(event.getPlayer().getLocation(), NPCPlayer.class,new GameProfileWrapper(UUID.randomUUID(),"test"));
		NPCAbstract entity = NPCLib.createRegistry(this).createNPC(event.getPlayer().getLocation(), NPCCaveSpider.class);
//		entity.getNpcEntity().setMethodWatcher(new MethodWatcher() {
//			@Override
//			public boolean methodCalled(Object thiz, String methodName, Object[] args) {
////				System.out.println("call: " + methodName);
////				if ("move".equals(methodName)) {
////					return false;
////				}
//				return super.methodCalled(thiz, methodName, args);
//			}
//
//			@Override
//			public Object methodCalled(Object thiz, String methodName, Object superValue, Object[] args) {
////				System.out.println("call: " + methodName);
//				return super.methodCalled(thiz, methodName, superValue, args);
//			}
//		});

//		WorldServer worldServer = ((CraftWorld) event.getPlayer().getLocation().getWorld()).getHandle();
//		((Entity) entity.getNpcEntity()).setLocation(event.getPlayer().getLocation().getX(), event.getPlayer().getLocation().getY(), event.getPlayer().getLocation().getZ(), 0f, 0f);
//		worldServer.addEntity((Entity) entity.getNpcEntity(), CreatureSpawnEvent.SpawnReason.CUSTOM);
		((Entity) entity.getNpcEntity()).getBukkitEntity().setCustomName("test");
		((Entity) entity.getNpcEntity()).getBukkitEntity().setCustomNameVisible(true);

		System.out.println(((Entity) entity.getNpcEntity()).getId());
		System.out.println(((Entity) entity.getNpcEntity()).dead);

		//		for (Player player : Bukkit.getOnlinePlayers()) {
		//			((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving((EntityLiving) entity));
		//		}

		Class superClass = entity.getClass();
		int c = 0;
		while ((superClass = superClass.getSuperclass()) != null) {
			System.out.println("super" + (c++) + ": " + superClass);
		}
		if (c == 0) {
			System.out.println("no superclass!");
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(CreatureSpawnEvent event) {
		System.out.println(event);
		System.out.println(event.isCancelled());
		event.setCancelled(false);
	}

}
