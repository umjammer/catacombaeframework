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

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import static java.lang.System.getLogger;


/**
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public class EncodedStringField extends StringRepresentableField {

    private static final Logger logger = getLogger(EncodedStringField.class.getName());

    private final byte[] fieldData;
    private final Charset charset;
    private final int offset;
    private final int length;

    public EncodedStringField(byte[] fieldData, String encoding) {
        this(fieldData, 0, fieldData.length, encoding);
    }

    public EncodedStringField(byte[] fieldData, int offset, int length, String encoding) {
        super("Byte[" + length + "]", FieldType.CUSTOM_CHARSET_STRING);
        this.fieldData = fieldData;
        this.offset = offset;
        this.length = length;
        this.charset = Charset.forName(encoding);
        String validateMsg = validate(this.fieldData, offset, length);
        if (validateMsg != null) {
            throw new IllegalArgumentException("Invalid value passed to constructor! Message: " + validateMsg);
        }
    }

    @Override
    public String validateStringValue(String s) {
        try {
            CharsetEncoder enc = charset.newEncoder();
            ByteBuffer bb = enc.encode(CharBuffer.wrap(s));
            byte[] array = bb.array();
            return validate(array, 0, array.length);
        } catch (CharacterCodingException cce) {
            logger.log(Level.DEBUG, cce.getMessage(), cce);
            return "Exception while encoding string data: " + cce;
        }
    }

    private String validate(byte[] data, int offset, int length) {
        if (length != fieldData.length)
            return "Invalid length for string. Was: " + length + " Should be: " + fieldData.length;
        // Attempt to decode data
        try {
            CharsetDecoder dec = charset.newDecoder();
            dec.decode(ByteBuffer.wrap(data, offset, length));
        } catch (Exception e) {
            logger.log(Level.DEBUG, e.getMessage(), e);
            return "Decode operation failed! Exception: " + e;
        }
        return null;
    }

    @Override
    public String getValueAsString() {
        try {
            CharsetDecoder dec = charset.newDecoder();
            return dec.decode(ByteBuffer.wrap(fieldData, offset, length)).toString();
        } catch (CharacterCodingException cce) {
            throw new IllegalStateException("Exception while decoding data...", cce);
        }
    }

    @Override
    public void setStringValue(String value) throws IllegalArgumentException {
        String validateMsg = validateStringValue(value);
        if (validateMsg == null) {
            try {
                CharsetEncoder enc = charset.newEncoder();
                ByteBuffer bb = enc.encode(CharBuffer.wrap(value));
                byte[] encodedData = bb.array();
                if (encodedData.length != length)
                    throw new RuntimeException("You should not see this.");
                System.arraycopy(encodedData, 0, fieldData, offset, length);
            } catch (CharacterCodingException cce) {
                throw new IllegalStateException("Exception while encoding string data: ", cce);
            }
        } else
            throw new IllegalArgumentException("Invalid string value! Message: " + validateMsg);
    }
}
