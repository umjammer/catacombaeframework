/*-
 * Copyright (C) 2008-2009 Erik Larsson
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

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.nio.ByteOrder;

import org.catacombae.util.Util;


/**
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public class IntegerField extends StringRepresentableField {

    private final DataHandle fieldData;
    private final int offset;
    private final IntegerFieldBits bits;
    private final Signedness signedness;
    private final ByteOrder endianness;
    private final BigInteger maxValue;
    private final BigInteger minValue;
    private final IntegerFieldRepresentation representation;

    public IntegerField(byte[] fieldData, IntegerFieldBits bits,
                        Signedness signedness, ByteOrder endianness) {
        this(fieldData, 0, bits, signedness, endianness);
    }

    public IntegerField(byte[] fieldData, int offset, IntegerFieldBits bits,
                        Signedness signedness, ByteOrder endianness) {
        this(fieldData, offset, bits, signedness, endianness,
                IntegerFieldRepresentation.DECIMAL, null);
    }

    public IntegerField(byte[] fieldData, int offset, IntegerFieldBits bits,
                        Signedness signedness, ByteOrder endianness,
                        IntegerFieldRepresentation representation, String unitComponent) {
        this(new ByteArrayDataHandle(fieldData), offset, bits, signedness,
                endianness, representation, unitComponent);
    }

    public IntegerField(Object obj, Field fieldData, int offset, IntegerFieldBits bits,
                        Signedness signedness, ByteOrder endianness,
                        IntegerFieldRepresentation representation, String unitComponent) {
        this(new IntegerFieldDataHandle(obj, fieldData, bits.getBytes()),
                offset, bits, signedness, endianness, representation,
                unitComponent);
    }

    public IntegerField(DataHandle fieldData, int offset, IntegerFieldBits bits,
                        Signedness signedness, ByteOrder endianness,
                        IntegerFieldRepresentation representation, String unitComponent) {
        super((signedness == Signedness.SIGNED ? "S" : "U") + "Int" + bits.getBits(),
                FieldType.INTEGER, unitComponent);
        // Input check
        if (fieldData == null)
            throw new IllegalArgumentException("fieldData == null");
        if (bits == null)
            throw new IllegalArgumentException("bits == null");
        if (signedness == null)
            throw new IllegalArgumentException("signedness == null");
        if (endianness == null)
            throw new IllegalArgumentException("endianness == null");
        if (representation == null)
            throw new IllegalArgumentException("representation == null");
        if (fieldData.getLength() - offset < bits.getBytes())
            throw new IllegalArgumentException("Not enough data left in fieldData!");
        this.fieldData = fieldData;
        this.offset = offset;
        this.bits = bits;
        this.signedness = signedness;
        this.endianness = endianness;
        this.representation = representation;
        byte[] maxValueBytes = new byte[bits.getBytes()];
        byte[] minValueBytes = new byte[bits.getBytes()];
        Util.set(maxValueBytes, (byte) 255);
        Util.zero(minValueBytes);
        if (signedness == Signedness.SIGNED) {
            maxValueBytes[0] = (byte) (maxValueBytes[0] & 127);
            minValueBytes[0] = (byte) 128;
        }
        this.maxValue = new BigInteger(1, maxValueBytes);
        this.minValue = new BigInteger(minValueBytes);
        String validateMsg = validateData();
        if (validateMsg != null) {
            throw new IllegalArgumentException("Invalid value passed to constructor! Message: " + validateMsg);
        }
    }

    private String validateData() {
        return validate(getValueAsBigInteger());
    }

    private String validate(BigInteger bi) {
        if (signedness != Signedness.SIGNED && bi.signum() == -1) {
            return "Tried to insert negative value into unsigned field.";
        } else if (bi.compareTo(maxValue) > 0) {
            return "Value too large for field! Maximum value is " + maxValue.toString() + ".";
        } else if (bi.compareTo(minValue) < 0) {
            return "Value too small for this field. Minimum value is " + minValue.toString() + ".";
        } else {
            return null;
        }
    }

    public BigInteger getValueAsBigInteger() {
        byte[] data;
        if (endianness == ByteOrder.LITTLE_ENDIAN)
            data = Util.byteSwap(fieldData.getBytesAsCopy(offset, bits.getBytes())); // Util.createReverseCopy(fieldData, offset, bits.getBytes());
        else if (endianness == ByteOrder.BIG_ENDIAN)
            data = fieldData.getBytesAsCopy(offset, bits.getBytes()); // Util.createCopy(fieldData, offset, bits.getBytes());
        else
            throw new RuntimeException("Illegal endianness value: " + endianness);
        if (signedness == Signedness.SIGNED)
            return new BigInteger(data);
        else if (signedness == Signedness.UNSIGNED)
            return new BigInteger(1, data);
        else
            throw new RuntimeException("Illegal signedness value: " + signedness);
    }

    @Override
    public String getValueAsString() {
        return representation.getPrefix() +
                getValueAsBigInteger().toString(representation.getRadix());
    }

    @Override
    public void setStringValue(String value) throws IllegalArgumentException {
        String validateMsg = validateStringValue(value);
        if (validateMsg == null) {
            BigInteger bi = new BigInteger(value);
            byte[] ba = bi.toByteArray();
            if (signedness == Signedness.SIGNED && ba.length != bits.getBytes())
                throw new RuntimeException("UNEXPECTED: ba.length (" +
                        ba.length + ") != bits.getBytes()(" + bits.getBytes() +
                        ")");
            if (signedness == Signedness.UNSIGNED && ba.length != (bits.getBytes() + 1))
                throw new RuntimeException("UNEXPECTED: ba.length (" +
                        ba.length + ") != bits.getBytes() + 1(" +
                        bits.getBytes() + "+1=" + (bits.getBytes() + 1) + ")");
            byte[] trueContents;
            if (endianness == ByteOrder.LITTLE_ENDIAN)
                trueContents = Util.createReverseCopy(ba,
                        ba.length - bits.getBytes(), bits.getBytes());
            else if (endianness == ByteOrder.BIG_ENDIAN)
                trueContents = Util.createCopy(ba, ba.length - bits.getBytes(),
                        bits.getBytes());
            else
                throw new RuntimeException("Illegal endianness value: " + endianness);
            System.arraycopy(trueContents, 0, fieldData, offset, bits.getBytes());
        } else
            throw new IllegalArgumentException("Invalid string value! Message: " + validateMsg);
    }

    @Override
    public String validateStringValue(String s) {
        try {
            BigInteger bi = new BigInteger(s);
            return validate(bi);
        } catch (NumberFormatException nfe) {
            return "Invalid integer string.";
        }
    }
}
