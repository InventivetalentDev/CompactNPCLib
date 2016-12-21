package org.inventivetalent.npclib.path;

import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.vectors.d3.Vector3DDouble;
import org.inventivetalent.vectors.d3.Vector3DInt;

import javax.annotation.Nonnull;

public class NMSPathPoint extends PathPoint {

	static final Class<?>            nmsPathPoint                 = Reflection.nmsClassResolver.resolveSilent("PathPoint");
	static final FieldResolver       PathPointFieldResolver       = new FieldResolver(nmsPathPoint);
	static final ConstructorResolver PathPointConstructorResolver = new ConstructorResolver(nmsPathPoint);

	private final Object handle;

	public NMSPathPoint(Vector3DDouble doubleVector) {
		this(new Vector3DInt(doubleVector));
	}

	public NMSPathPoint(Vector3DInt intVector) {
		this(intVector.getX(), intVector.getY(), intVector.getZ());
	}

	public NMSPathPoint(int x, int y, int z) {
		super(x, y, z);
		this.handle = PathPointConstructorResolver.resolveWrapper(new Class[] {
				int.class,
				int.class,
				int.class }).newInstance(x, y, z);
	}

	public NMSPathPoint(@Nonnull Object handle) {
		super((int) PathPointFieldResolver.resolveWrapper("a").get(handle), (int) PathPointFieldResolver.resolveWrapper("b").get(handle), (int) PathPointFieldResolver.resolveWrapper("c").get(handle));
		this.handle = handle;
	}

	public Object getHandle() {
		return this.handle;
	}
}
