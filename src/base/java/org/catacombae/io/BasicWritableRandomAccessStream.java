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
 *
 * @author <a href="http://hem.bredband.net/catacombae">Erik Larsson</a>
 */
public abstract class BasicWritableRandomAccessStream extends BasicWritable implements WritableRandomAccessStream {
    
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
}
