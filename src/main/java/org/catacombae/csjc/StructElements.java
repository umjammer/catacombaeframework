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

import org.catacombae.csjc.structelements.Dictionary;
import org.catacombae.csjc.structelements.FieldType;
import org.catacombae.csjc.structelements.IntegerFieldBits;
import org.catacombae.csjc.structelements.IntegerFieldRepresentation;
import org.catacombae.csjc.structelements.Signedness;


/**
 * Interface to implement when a struct wants to present detailed information on
 * its members to an external program, also allowing its members to be modified
 * externally in a controlled way.
 *
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 * @see org.catacombae.csjc.structelements.StructElement
 */
public interface StructElements {

    /** Shorthand constant. */
    Signedness SIGNED = Signedness.SIGNED;
    /** Shorthand constant. */
    Signedness UNSIGNED = Signedness.UNSIGNED;
    /** Shorthand constant. */
    FieldType BOOLEAN = FieldType.BOOLEAN;
    /** Shorthand constant. */
    FieldType INTEGER = FieldType.INTEGER;
    /** Shorthand constant. */
    FieldType BYTEARRAY = FieldType.BYTEARRAY;
    /** Shorthand constant. */
    FieldType ASCIISTRING = FieldType.ASCIISTRING;
    /** Shorthand constant. */
    FieldType CUSTOM_CHARSET_STRING = FieldType.CUSTOM_CHARSET_STRING;
    /** Shorthand constant. */
    FieldType DATE = FieldType.DATE;
    /** Shorthand constant. */
    IntegerFieldBits BITS_8 = IntegerFieldBits.BITS_8;
    /** Shorthand constant. */
    IntegerFieldBits BITS_16 = IntegerFieldBits.BITS_16;
    /** Shorthand constant. */
    IntegerFieldBits BITS_32 = IntegerFieldBits.BITS_32;
    /** Shorthand constant. */
    IntegerFieldBits BITS_64 = IntegerFieldBits.BITS_64;
    /** Shorthand constant. */
    IntegerFieldRepresentation DECIMAL = IntegerFieldRepresentation.DECIMAL;
    /** Shorthand constant. */
    IntegerFieldRepresentation HEXADECIMAL = IntegerFieldRepresentation.HEXADECIMAL;
    /** Shorthand constant. */
    IntegerFieldRepresentation OCTAL = IntegerFieldRepresentation.OCTAL;
    /** Shorthand constant. */
    IntegerFieldRepresentation BINARY = IntegerFieldRepresentation.BINARY;

    /**
     * Shorthand subclass, so the user doesn't have to import DictionaryBuilder in every
     * implementation of StructElements.
     */
    class DictionaryBuilder extends org.catacombae.csjc.structelements.DictionaryBuilder {

        /**
         * @see org.catacombae.csjc.structelements.DictionaryBuilder#DictionaryBuilder(java.lang.String)
         */
        public DictionaryBuilder(String typeName) {
            super(typeName);
        }

        /**
         * @see org.catacombae.csjc.structelements.DictionaryBuilder#DictionaryBuilder(java.lang.String, java.lang.String)
         */
        public DictionaryBuilder(String typeName, String typeDescription) {
            super(typeName, typeDescription);
        }
    }

    /**
     * Returns a dictionary of the elements of this data structure. The keys in
     * the dictionary should be the respective variable names, and the elements
     * should provide access to all the fields of the data structure.
     *
     * @return a dictionary of the elements of this data structure.
     */
    Dictionary getStructElements();
}
