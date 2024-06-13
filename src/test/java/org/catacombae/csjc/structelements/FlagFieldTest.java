package org.catacombae.csjc.structelements;

import org.catacombae.util.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FlagFieldTest {

    @Test
    void test1() {
        byte[] i = new byte[] {0x00, (byte) 0xFF, 0x00, (byte) 0xEE};
        System.err.println("(1) 0x" + Util.byteArrayToHexString(i));
        assertEquals("00ff00ee", Util.byteArrayToHexString(i));

        FlagField ff = new FlagField(i, 1, 2, 15);

        System.err.println("Flag set to: " + ff.getValueAsBoolean());
        System.err.println("Setting flag to true.");
        ff.setBooleanValue(true);
        System.err.println("(2) 0x" + Util.byteArrayToHexString(i));
        assertEquals("00ff00ee", Util.byteArrayToHexString(i));

        System.err.println("Setting flag to false.");
        ff.setBooleanValue(false);
        System.err.println("(3) 0x" + Util.byteArrayToHexString(i));
        assertEquals("007f00ee", Util.byteArrayToHexString(i));

        System.err.println("Setting flag to true again.");
        ff.setBooleanValue(true);
        System.err.println("(4) 0x" + Util.byteArrayToHexString(i));
        assertEquals("00ff00ee", Util.byteArrayToHexString(i));
    }
}