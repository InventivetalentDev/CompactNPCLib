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

package org.inventivetalent.npclib.npc.living;

import org.bukkit.entity.LivingEntity;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.entity.living.NPCEntityLiving;
import org.inventivetalent.npclib.npc.NPCAbstract;
import org.inventivetalent.npclib.watcher.Watch;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.ResolverQuery;
import org.inventivetalent.vectors.d3.Vector3DDouble;

public abstract class NPCLivingAbstract<N extends NPCEntityLiving, B extends LivingEntity> extends NPCAbstract<N, B> {

	protected FieldResolver  entityLivingFieldResolver  = new FieldResolver(Reflection.nmsClassResolver.resolveSilent("EntityLiving"));
	protected MethodResolver entityLivingMethodResolver = new MethodResolver(Reflection.nmsClassResolver.resolveSilent("EntityLiving"));

	protected NPCLivingAbstract(N npcEntity) {
		super(npcEntity);
	}

	@Watch("die(DamageSource)")
	public boolean onDie(Object damageSource) {
		System.out.println("onDie -> NPCLivingAbstract");
		//TODO: NPCDeathEvent (with damage source)
		return true;
	}

	public void setYaw(float yaw) {
		yaw = clampYaw(yaw);
		getNpcEntity().setYaw(yaw);
		entityLivingFieldResolver.resolveWrapper("aO").set(getNpcEntity(), yaw);
	}

	public void setPitch(float pitch) {
		getNpcEntity().setPitch(pitch);
	}

	protected float clampYaw(float yaw) {
		while (yaw < -180.0F) {
			yaw += 360.0F;
		}

		while (yaw >= 180.0F) {
			yaw -= 360.0F;
		}

		return yaw;
	}

	public void lookAt(Vector3DDouble vector) {
		double dx = vector.getX() - getBukkitEntity().getEyeLocation().getX();
		double dy = vector.getY() - getBukkitEntity().getEyeLocation().getY();
		double dz = vector.getZ() - getBukkitEntity().getEyeLocation().getZ();
		double xzd = Math.sqrt(dx * dx + dz * dz);
		double yd = Math.sqrt(xzd * xzd + dy * dy);

		double yaw = Math.toDegrees(Math.atan2(dz, dx)) - 90.0D;
		double pitch = -Math.toDegrees(Math.atan2(dy, yd));

		setYaw((float) yaw);
		setPitch((float) pitch);
	}

	public Object getEntityLivingField(String field) {
		return entityLivingFieldResolver.resolveWrapper(field).get(getNpcEntity());
	}

	public void setEntityLivingField(String field, Object value) {
		entityLivingFieldResolver.resolveWrapper(field).set(getNpcEntity(), value);
	}

	public Object invokeEntityLivingMethod(String method, Class[] types, Object[] args) {
		return entityLivingMethodResolver.resolveWrapper(new ResolverQuery(method, types)).invoke(getNpcEntity(), args);
	}

	public Object invokeEntityLivingMethod(String method, Object... args) {
		return entityLivingMethodResolver.resolveWrapper(method).invoke(getNpcEntity(), args);
	}

}
