/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.catacombae.util.test;

import org.catacombae.util.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
class TestByteSwap {

    @Test
    void test1() {
        byte[] testArray = new byte[] {
                0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7,
                0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF,
                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
                0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F
        };

        short testShort = (short) 0xFEDC;
        char testChar = (char) 0xBA98;
        int testInt = 0xFEDCBA98;
        long testLong = 0xFEDCBA9876543210L;

        // byte array
        assertEquals("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f", Util.byteArrayToHexString(testArray));
        assertEquals("1f1e1d1c1b1a191817161514131211100f0e0d0c0b0a09080706050403020100", Util.byteArrayToHexString(Util.byteSwap(testArray)));
        // short
        assertEquals("fedc", Util.toHexStringBE(testShort));
        assertEquals("dcfe", Util.toHexStringBE(Util.byteSwap(testShort)));
        // char
        assertEquals("ba98", Util.toHexStringBE(testChar));
        assertEquals("98ba", Util.toHexStringBE(Util.byteSwap(testChar)));
        // int
        assertEquals("fedcba98", Util.toHexStringBE(testInt));
        assertEquals("98badcfe", Util.toHexStringBE(Util.byteSwap(testInt)));
        // long
        assertEquals("fedcba9876543210", Util.toHexStringBE(testLong));
        assertEquals("1032547698badcfe", Util.toHexStringBE(Util.byteSwap(testLong)));
    }
}
