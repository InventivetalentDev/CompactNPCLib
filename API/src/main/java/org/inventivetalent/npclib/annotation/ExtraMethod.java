package org.inventivetalent.npclib.annotation;

import org.inventivetalent.reflection.minecraft.Minecraft;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtraMethod {

	String value();

	/**
	 * If specified, the annotated method will be generate for this version upwards
	 * @return the Version
	 */
	Minecraft.Version fromVersion() default Minecraft.Version.UNKNOWN;

	/**
	 * If specified, the annotated method will be generate for this version downwards
	 * @return the Version
	 */
	Minecraft.Version untilVersion() default Minecraft.Version.UNKNOWN;

	/**
	 * If specified, the annotated method will be generate for only these versions
	 * @return the Version
	 */
	Minecraft.Version[] forVersion() default {};

}
