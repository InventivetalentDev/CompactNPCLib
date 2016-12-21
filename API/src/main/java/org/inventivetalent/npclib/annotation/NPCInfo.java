package org.inventivetalent.npclib.annotation;

import lombok.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.npclib.entity.NPCEntity;

import java.lang.reflect.Modifier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.emptyToNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
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
	private       Class<?>                   npcClass;

	public static NPCInfo of(NPC annotation) {
		checkNotNull(annotation);
		return new NPCInfo(annotation.id(), checkNotNull(annotation.type()), checkNotNull(annotation.bukkit()), checkNotNull(emptyToNull(annotation.nms())), checkNotNull(annotation.entity()), annotation.constructors(), annotation.extraPackages(), annotation.extraFields(), annotation.extraMethods());
	}

	public static NPCInfo of(Class<?> clazz) {
		checkNotNull(clazz);
		checkArgument(!Modifier.isAbstract(clazz.getModifiers()), "Cannot use @NPC on abstract class");
		checkArgument(!clazz.isInterface(), "Cannot use @NPC on interface");
		NPC annotation = (NPC) clazz.getAnnotation(NPC.class);
		NPCInfo info = of(checkNotNull(annotation, "Class has no @NPC annotation"));
		info.npcClass = clazz;
		return info;
	}

	public Class<?> getNMSClass() {
		try {
			return Reflection.nmsClassResolver.resolve(nms);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public String getNPCClassName() {
		String className = npcClass.getName();
		if (className.startsWith("org.inventivetalent.npclib.npc")) {// Internal NPCs
			return "NPC" + nms;
		} else {
			return className.replace(".", "_") + "_NPC";
		}
	}

}
