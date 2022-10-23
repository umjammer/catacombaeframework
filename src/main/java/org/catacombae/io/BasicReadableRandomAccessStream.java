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
 * Basic implementation of many features of ReadableRandomAccessStream, to allow the subclasser to
 * only implement the essential methods needed to support a ReadableRandomAccessStream.
 * 
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public abstract class BasicReadableRandomAccessStream extends BasicReadable implements ReadableRandomAccessStream {
    /**
     * Empty constructor (there is no state maintained in this class).
     */
    protected BasicReadableRandomAccessStream() { }
    
    /** {@inheritDoc} */
    public abstract void close() throws RuntimeIOException;

    /** {@inheritDoc} */
    public abstract void seek(long pos) throws RuntimeIOException;

    /** {@inheritDoc} */
    public abstract long length() throws RuntimeIOException;

    /** {@inheritDoc} */
    public abstract long getFilePointer() throws RuntimeIOException;

}
