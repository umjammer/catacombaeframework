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
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catacombae.csjc;

/**
 * Represents a dynamic struct, i.e. a struct with a size that varies between
 * instances.
 *
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public interface DynamicStruct extends AbstractStruct {
    public int maxSize();
    public int occupiedSize();
}
