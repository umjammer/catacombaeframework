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
 * This is the superinterface of all streams in the catacombae.io package. It contains the only
 * method that is general enough to be applicable to all streams, the <code>close()</code> method.
 * 
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public interface Stream {
    /**
     * Closes the stream and releases all associated resources.
     * 
     * @throws org.catacombae.io.RuntimeIOException if an I/O error occurred, preventing the stream
     * to close.
     */
    public void close() throws RuntimeIOException;
}
