package org.catacombae.util;

import java.io.PrintStream;
import java.text.DecimalFormat;

import org.junit.jupiter.api.Test;

import static org.catacombae.util.SpeedUnitUtils.roundDoubleToDecimals;
import static org.junit.jupiter.api.Assertions.assertEquals;


class SpeedUnitUtilsTest {

    @Test
    void test1() {
        PrintStream ps = System.err;
        ps.println("Testing roundDoubleToDecimals:");
        ps.println("  Pi: " + Math.PI);
        ps.println("  Pi 0: " + roundDoubleToDecimals(Math.PI, 0));
        assertEquals(3.0, roundDoubleToDecimals(Math.PI, 0));
        ps.println("  Pi 1: " + roundDoubleToDecimals(Math.PI, 1));
        assertEquals(3.1, roundDoubleToDecimals(Math.PI, 1));
        ps.println("  Pi 2: " + roundDoubleToDecimals(Math.PI, 2));
        assertEquals(3.14, roundDoubleToDecimals(Math.PI, 2));
        ps.println("  Pi 4: " + roundDoubleToDecimals(Math.PI, 4));
        assertEquals(3.1415, roundDoubleToDecimals(Math.PI, 4));
        ps.println("  Pi 44: " + roundDoubleToDecimals(Math.PI, 44));
        assertEquals(3.141592653589793, roundDoubleToDecimals(Math.PI, 44));

        ps.println("Testing different DecimalFormat variants:");
        DecimalFormat[] fmts = new DecimalFormat[] {
                new DecimalFormat("0"),
                new DecimalFormat("0.0"),
                new DecimalFormat("0.00"),
                new DecimalFormat("0.000"),
                new DecimalFormat("0.0000"),
                new DecimalFormat("0.00000"),
        };

        String[] expectedFormats = {
                "3", "3.1", "3.14", "3.142", "3.1416", "3.14159"
        };

        for (int i = 0; i < fmts.length; ++i) {
            DecimalFormat fmt = fmts[i];
            ps.println("  Entry " + i + ":");
            ps.println("    getMaximumIntegerDigits()=" + fmt.getMaximumIntegerDigits());
            ps.println("    getMinimumIntegerDigits()=" + fmt.getMinimumIntegerDigits());
            ps.println("    getMaximumFractionDigits()=" + fmt.getMaximumFractionDigits());
            ps.println("    getMinimumFractionDigits()=" + fmt.getMinimumFractionDigits());
            ps.println("    format(Pi)=" + fmt.format(Math.PI));
            assertEquals(expectedFormats[i], fmt.format(Math.PI));
            ps.println("    format(rounded Pi)=" + fmt.format(roundDoubleToDecimals(Math.PI, fmt.getMaximumFractionDigits())));
        }
    }
}