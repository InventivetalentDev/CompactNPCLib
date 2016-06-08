package org.inventivetalent.npclib.annotation;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.inventivetalent.npclib.entity.NPCEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NPC {

	int id();

	EntityType type();

	Class<? extends Entity> bukkit();

	String nms();

	Class<? extends NPCEntity> entity();

	String[] constructors() default {
			"public %1$s(World world){\n"
					+ "  super(world);\n"
					+ "}" };

	String[] extraPackages() default {};

	String[] extraFields() default {};

	String[] extraMethods() default {};

}
