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
public class FlagField extends BooleanRepresentableField {

    private final byte[] fieldData;
    private final int offset;
    private final int length;
    private final int bitNumber;

    public FlagField(byte[] fieldData, int bitNumber) {
        this(fieldData, 0, fieldData.length, bitNumber);
    }

    public FlagField(byte[] fieldData, int offset, int length, int bitNumber) {
        super("Bit[1]", FieldType.BOOLEAN);
        this.fieldData = fieldData;
        this.offset = offset;
        this.length = length;
        this.bitNumber = bitNumber;
        if (bitNumber < 0 || bitNumber > length * 8)
            throw new IllegalArgumentException("Illegal bit address! Valid " + "addresses are in the range 0 to " + (length * 8 - 1));
    }

    @Override
    public boolean getValueAsBoolean() {
//        System.err.println("getValueAsBoolean(): bitNumber/8 = " + (bitNumber / 8));
        int byteNumber = (length - 1) - (bitNumber / 8);
//        System.err.println("getValueAsBoolean(): byteNumber = " + byteNumber);
        byte flagByte = fieldData[offset + byteNumber];
//        System.err.println("getValueAsBoolean(): bitNumber%8 = " + (bitNumber % 8));
        int flag = (flagByte >> (bitNumber % 8)) & 1;
        return flag != 0;
    }

    @Override
    public void setBooleanValue(boolean b) {
        int byteNumber = (length - 1) - (bitNumber / 8);
        byte flagByte = fieldData[offset + byteNumber];
        int bitmask = 1 << (bitNumber % 8);
        byte modifiedFlagByte;
        if (b)
            modifiedFlagByte = (byte) (flagByte | bitmask);
        else
            modifiedFlagByte = (byte) ~((~flagByte) | bitmask);
        fieldData[offset + byteNumber] = modifiedFlagByte;
    }
}
