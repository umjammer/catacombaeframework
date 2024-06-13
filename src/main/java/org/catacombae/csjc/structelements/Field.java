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

package org.catacombae.csjc.structelements;

/**
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public abstract class Field extends StructElement {

    private final FieldType type;

    protected Field(String typeName, FieldType type) {
        this(typeName, null, type);
    }

    protected Field(String typeName, String typeDescription, FieldType type) {
        super(typeName, typeDescription);
        this.type = type;
    }

    public FieldType getType() {
        return type;
    }
}
