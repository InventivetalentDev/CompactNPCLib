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
