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

import org.inventivetalent.npclib.Reflection;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.vectors.d3.Vector3DDouble;
import org.inventivetalent.vectors.d3.Vector3DInt;

import javax.annotation.Nonnull;

public class NMSPathPoint extends PathPoint {

	static final Class               nmsPathPoint                 = Reflection.nmsClassResolver.resolveSilent("PathPoint");
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
