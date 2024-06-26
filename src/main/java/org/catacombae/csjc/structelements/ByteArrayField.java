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

import org.catacombae.util.Util;


/**
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public class ByteArrayField extends StringRepresentableField {

    private final byte[] fieldData;
    private final int offset;
    private final int length;

    public ByteArrayField(byte[] fieldData) {
        this(fieldData, 0, fieldData.length);
    }

    public ByteArrayField(byte[] fieldData, int offset, int length) {
        super("Byte[" + fieldData.length + "]", FieldType.BYTEARRAY);
        this.fieldData = fieldData;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public String getValueAsString() {
        return "0x" + Util.byteArrayToHexString(fieldData, offset, length);
    }

    @Override
    public void setStringValue(String value) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Can't set byte string to string value at this point.");
    }

    @Override
    public String validateStringValue(String s) {
        return "Can't set a byte string to a string value.";
    }
}
