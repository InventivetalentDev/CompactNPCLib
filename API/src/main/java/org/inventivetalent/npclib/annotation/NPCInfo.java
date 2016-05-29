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

package org.inventivetalent.npclib.annotation;

import lombok.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.entity.living.human.NPCEntity;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.emptyToNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@EqualsAndHashCode
@ToString
public class NPCInfo {

	private final int                        id;
	private final EntityType                 type;
	private final Class<? extends Entity>    bukkit;
	private final String                     nms;
	private final Class<? extends NPCEntity> entity;
	private final String[]                   constructors;
	private final String[]                   extraPackages;
	private final String[]                   extraFields;
	private final String[]                   extraMethods;

	public Class<?> getNMSClass() {
		try {
			return Reflection.nmsClassResolver.resolve(nms);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public String getNPCClassName() {
		return "NPC" + nms;
	}

	public static NPCInfo of(NPC annotation) {
		checkNotNull(annotation);
		return new NPCInfo(annotation.id(), checkNotNull(annotation.type()), checkNotNull(annotation.bukkit()), checkNotNull(emptyToNull(annotation.nms())), checkNotNull(annotation.entity()), annotation.constructors(), annotation.extraPackages(), annotation.extraFields(), annotation.extraMethods());
	}

	public static NPCInfo of(Class clazz) {
		NPC annotation = (NPC) checkNotNull(clazz).getAnnotation(NPC.class);
		return of(checkNotNull(annotation, "Class has no @NPC annotation"));
	}

}
