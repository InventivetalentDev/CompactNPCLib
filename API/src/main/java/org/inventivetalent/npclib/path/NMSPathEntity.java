package org.inventivetalent.npclib.path;

import com.google.common.collect.Iterables;
import com.google.common.collect.ObjectArrays;
import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.vectors.d3.Vector3DDouble;

public class NMSPathEntity {

	static final Class<?>            nmsPathEntity                 = Reflection.nmsClassResolver.resolveSilent("PathEntity");
	static final Class<?>            nmsVector                     = Reflection.nmsClassResolver.resolveSilent("Vec3D");
	static final Class<?>            nmsEntity                     = Reflection.nmsClassResolver.resolveSilent("Entity");
	static final FieldResolver       PathEntityFieldResolver       = new FieldResolver(nmsPathEntity);
	static final FieldResolver       Vec3DFieldResolver            = new FieldResolver(nmsVector);
	static final ConstructorResolver PathEntityConstructorResolver = new ConstructorResolver(nmsPathEntity);
	static final MethodResolver      PathEntityMethodResolver      = new MethodResolver(nmsPathEntity);

	private final Object handle;

	public NMSPathEntity(Object handle) {
		this.handle = handle;
	}

	public NMSPathEntity(Object[] pointHandles) throws ReflectiveOperationException {
		this.handle = PathEntityConstructorResolver.resolveFirstConstructor().newInstance(new Object[] { pointHandles });
	}

	public NMSPathEntity(NMSPathPoint[] points) throws ReflectiveOperationException {
		this(getHandles(points));
	}

	public NMSPathEntity(Iterable<NMSPathPoint> points) throws ReflectiveOperationException {
		this(getHandles(points));
	}

	public void nextIndex() {
		try {
			nmsPathEntity.getMethod("a").invoke(handle);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isFinished() {
		try {
			return (boolean) nmsPathEntity.getMethod("b").invoke(handle);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public NMSPathPoint getTargetPoint() {
		try {
			return new NMSPathPoint(nmsPathEntity.getMethod("c").invoke(handle));
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public NMSPathPoint getPoint(int index) {
		try {
			return new NMSPathPoint(nmsPathEntity.getMethod("a", int.class).invoke(handle, index));
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public int getLength() {
		try {
			return (int) nmsPathEntity.getMethod("d").invoke(handle);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public int getIndex() {
		try {
			return (int) nmsPathEntity.getMethod("e").invoke(handle);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public Object getVectorHandle(Object entityHandle, int index) {
		try {
			return nmsPathEntity.getMethod("e", nmsEntity, int.class).invoke(handle, entityHandle, index);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public Object getVectorHandle(Object entityHandle) {
		try {
			return nmsPathEntity.getMethod("a", nmsEntity).invoke(handle, entityHandle);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public Vector3DDouble getVector(Object entityHandle) {
		return nmsToVector(getVectorHandle(entityHandle));
	}

	static Vector3DDouble nmsToVector(Object vectorHandle) {
		try {
			return new Vector3DDouble(
					(double) Vec3DFieldResolver.resolve("x", "a").get(vectorHandle),
					(double) Vec3DFieldResolver.resolve("y", "b").get(vectorHandle),
					(double) Vec3DFieldResolver.resolve("z", "c").get(vectorHandle));
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	static Object[] getHandles(NMSPathPoint[] points) {
		Object[] handles = ObjectArrays.newArray(NMSPathPoint.nmsPathPoint, points.length);
		for (int i = 0; i < points.length; i++) {
			handles[i] = points[i].getHandle();
		}
		return handles;
	}

	static Object[] getHandles(Iterable<NMSPathPoint> points) {
		return getHandles(Iterables.toArray(points, NMSPathPoint.class));
	}

}
