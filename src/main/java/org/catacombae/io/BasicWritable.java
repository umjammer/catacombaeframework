/*-
 * Copyright (C) 2008 Erik Larsson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.catacombae.io;

/**
 * Basic implementation of core features of Writable, to allow the subclasser to only implement the
 * essential methods.
 * 
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public abstract class BasicWritable implements Writable {
    /**
     * Empty constructor (there is no state maintained in this class).
     */
    protected BasicWritable() { }

    /** {@inheritDoc} */
    public void write(byte[] b) throws RuntimeIOException {
        defaultWrite(this, b);
    }

    /** {@inheritDoc} */
    public abstract void write(byte[] b, int off, int len) throws RuntimeIOException;

    /** {@inheritDoc} */
    public void write(int b) throws RuntimeIOException {
        defaultWrite(this, b);
    }

    static void defaultWrite(Writable w, byte[] b) throws RuntimeIOException {
        w.write(b, 0, b.length);
    }

    static void defaultWrite(Writable w, int b) throws RuntimeIOException {
        w.write(new byte[] { (byte)(b & 0xFF) }, 0, 1);
    }
}
