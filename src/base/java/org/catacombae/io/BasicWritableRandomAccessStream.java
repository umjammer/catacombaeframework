/*-
 * Copyright (C) 2008 Erik Larsson
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catacombae.io;

/**
 *
 * @author <a href="http://hem.bredband.net/catacombae">Erik Larsson</a>
 */
public abstract class BasicWritableRandomAccessStream implements WritableRandomAccessStream {
    
    /**
     * Empty constructor (there is no state maintained in this class).
     */
    protected BasicWritableRandomAccessStream() { }
    
    /** {@inheritDoc} */
    public abstract void close() throws RuntimeIOException;

    /** {@inheritDoc} */
    public abstract void seek(long pos) throws RuntimeIOException;

    /** {@inheritDoc} */
    public abstract long length() throws RuntimeIOException;

    /** {@inheritDoc} */
    public abstract long getFilePointer() throws RuntimeIOException;

    /** {@inheritDoc} */
    public void write(byte[] b) {
        defaultWrite(this, b);
    }

    /** {@inheritDoc} */
    public abstract void write(byte[] b, int off, int len);

    /** {@inheritDoc} */
    public void write(int b) {
        defaultWrite(this, b);
    }
    
    static void defaultWrite(Writable w, byte[] b) {
        w.write(b, 0, b.length);
    }
    
    static void defaultWrite(Writable w, int b) {
        w.write(new byte[] { (byte)(b & 0xFF) }, 0, 1);
    }
}
