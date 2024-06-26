/*-
 * Copyright (C) 2020 Erik Larsson
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
 * Interface with common methods implemented by file-backed streams.
 *
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public interface AbstractFileStream {

    /**
     * Get the path passed to the underlying file open interface.
     * <p>
     * This is informational only and only represents the file path at open time
     * and doesn't trace any renames or moves that may have happened after it
     * was opened (i.e. it's not guaranteed to exist.
     *
     * @return the path passed to the underlying file open interface.
     */
    String getOpenPath();
}
