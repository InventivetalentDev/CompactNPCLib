package org.inventivetalent.npclib.watcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Watch {

	/**
	 * Possible method signatures to watch. If empty, this method's signature will be used.
	 *
	 * @return the watched method signature(s)
	 */
	String[] value() default {};

	/**
	 * Alternative for void methods to decide whether super is called. (Alternatively just return true/false)
	 *
	 * @return if <code>true</code>, the super method will be called
	 */
	boolean passThrough() default true;

	/**
	 * Whether the annotated methods uses {@link org.inventivetalent.npclib.ObjectContainer}s as its parameters. Defaults to <code>true</code>
	 *
	 * @return <code>true</code> if all parameters of the annotated method are {@link org.inventivetalent.npclib.ObjectContainer}s, <code>false</code> if the parameters are raw types
	 */
	boolean containers() default true;

	//	/**
	//	 * Whether to exclude the <code>this</code> parameter
	//	 *
	//	 * @return exclude <code>this</code>
	//	 */
	//	boolean ignoreThiz() default false;
}
