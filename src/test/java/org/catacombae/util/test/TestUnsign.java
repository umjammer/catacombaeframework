/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.catacombae.util.test;

import java.math.BigInteger;

import org.catacombae.util.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
class TestUnsign {

    @Test
    void test() {
        byte bLow = 0x0, bMiddle = 0x7f, bHigh = (byte) 0xff;
        short sLow = 0x0, sMiddle = 0x7fff, sHigh = (short) 0xffff;
        char cLow = 0x0, cMiddle = 0x7fff, cHigh = 0xffff;
        int iLow = 0x0, iMiddle = 0x7fff_ffff, iHigh = 0xffff_ffff;
        long lLow = 0x0, lMiddle = 0x7fff_ffff_ffff_ffffL, lHigh = 0xffff_ffff_ffff_ffffL;

        // byte
        assertEquals(0, bLow);
        assertEquals(0, Util.unsign(bLow));
        assertEquals(127, bMiddle);
        assertEquals(127, Util.unsign(bMiddle));
        assertEquals(-1, bHigh);
        assertEquals(255, Util.unsign(bHigh));

        // short
        assertEquals(0, sLow);
        assertEquals(0, Util.unsign(sLow));
        assertEquals(32767, sMiddle);
        assertEquals(32767, Util.unsign(sMiddle));
        assertEquals(-1, sHigh);
        assertEquals(65535, Util.unsign(sHigh));

        // char
        assertEquals((char) 0, cLow);
        assertEquals(0, Util.unsign(cLow));
        assertEquals('翿', cMiddle);
        assertEquals(32767, Util.unsign(cMiddle));
        assertEquals('￿', cHigh);
        assertEquals(65535, Util.unsign(cHigh));

        // int
        assertEquals(0, iLow);
        assertEquals(0, Util.unsign(iLow));
        assertEquals(2147483647, iMiddle);
        assertEquals(2147483647, Util.unsign(iMiddle));
        assertEquals(-1, iHigh);
        assertEquals(4294967295L, Util.unsign(iHigh));

        // long
        assertEquals(0, lLow);
        assertEquals(BigInteger.ZERO, Util.unsign(lLow));
        assertEquals(9223372036854775807L, lMiddle);
        assertEquals(BigInteger.valueOf(9223372036854775807L), Util.unsign(lMiddle));
        assertEquals(-1, lHigh);
        assertEquals(new BigInteger("18446744073709551615"), Util.unsign(lHigh));
    }
}
