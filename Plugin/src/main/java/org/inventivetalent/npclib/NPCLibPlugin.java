package org.inventivetalent.npclib;

import net.minecraft.server.v1_9_R1.Entity;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.npclib.npc.living.NPCLivingAbstract;
import org.inventivetalent.npclib.npc.living.human.NPCPlayer;
import org.inventivetalent.vectors.d3.Vector3DDouble;

import java.util.UUID;

public class NPCLibPlugin extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		NPCLib.logger = getLogger();
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onInteract(final PlayerInteractEvent event) {
		if (!event.getPlayer().isSneaking()) { return; }

		//		NPCPlayer entity = NPCLib.createRegistry(this).createPlayerNPC(event.getPlayer().getLocation(), NPCPlayer.class,new GameProfileWrapper(UUID.randomUUID(),"test"));
		final NPCLivingAbstract entity = NPCLib.createRegistry(this).createPlayerNPC(event.getPlayer().getLocation(), NPCPlayer.class, UUID.randomUUID(), "test");
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
				//				entity.lookAt(new Vector3DDouble(event.getPlayer().getLocation()));
			}
		}, 10, 10);
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
				//				entity.moveWithHeading(0,0.5f);
				entity.pathfindTo(new Vector3DDouble(event.getPlayer().getLocation()), 0.2D);
			}
		}, 20, 80);
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
