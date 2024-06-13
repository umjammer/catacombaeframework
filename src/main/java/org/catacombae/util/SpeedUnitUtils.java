/*-
 * Copyright (C) 2006-2008 Erik Larsson
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
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.catacombae.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Utility class for getting a human readable string from a byte amount.
 *
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public class SpeedUnitUtils {
    protected static final long kibibyte = mypow(2, 10);
    protected static final long mebibyte = mypow(2, 20);
    protected static final long gibibyte = mypow(2, 30);
    protected static final long tebibyte = mypow(2, 40);
    protected static final long pebibyte = mypow(2, 50);
    protected static final long exbibyte = mypow(2, 60);
    protected static final long kilo = mypow(10, 3);
    protected static final long mega = mypow(10, 6);
    protected static final long giga = mypow(10, 9);
    protected static final long tera = mypow(10, 12);
    protected static final long peta = mypow(10, 15);
    protected static final long exa = mypow(10, 18);

    /**
     * Set this to change the standard output of bytesToBinaryUnit(long) and
     * bytesToDecimalBitUnit(long).
     */
    protected static final DecimalFormat standardUnitFormatter = new DecimalFormat("0");

    protected static String format(double d, DecimalFormat unitFormatter) {
        double rounded =
                roundDoubleToDecimals(d, unitFormatter.getMaximumFractionDigits());
        return unitFormatter.format(rounded);
    }
    
    public static double roundDoubleToDecimals(double d, int decimals) {
        long integerPart = (long)d;
        double remaining = d - integerPart;
        if(decimals > 0) {
            BigDecimal bd = new BigDecimal(remaining, new MathContext(decimals, RoundingMode.DOWN));
            return integerPart + bd.doubleValue();
        }
        else
            return (double)integerPart;
    }

    public static String bytesToBinaryUnit(long size) {
        return bytesToBinaryUnit(size, standardUnitFormatter);
    }

    public static String bytesToBinaryUnit(long size, DecimalFormat unitFormatter) {
        String result;

        if(size >= exbibyte)
            result = format(size / (double) exbibyte, unitFormatter) + " EiB";
        else if(size >= pebibyte)
            result = format(size / (double) pebibyte, unitFormatter) + " PiB";
        else if(size >= tebibyte)
            result = format(size / (double) tebibyte, unitFormatter) + " TiB";
        else if(size >= gibibyte)
            result = format(size / (double) gibibyte, unitFormatter) + " GiB";
        else if(size >= mebibyte)
            result = format(size / (double) mebibyte, unitFormatter) + " MiB";
        else if(size >= kibibyte)
            result = format(size / (double) kibibyte, unitFormatter) + " KiB";
        else
            result = size + " B";

        return result;
    }

    public static String bytesToDecimalBitUnit(long bytes) {
        return bytesToDecimalBitUnit(bytes, standardUnitFormatter);
    }

    public static String bytesToDecimalBitUnit(long bytes, DecimalFormat unitFormatter) {
        long bits = bytes * 8;
        String result;

        if(bits >= exa)
            result = format(bits / (double) exa, unitFormatter) + " Ebit";
        else if(bits >= peta)
            result = format(bits / (double) peta, unitFormatter) + " Pbit";
        else if(bits >= tera)
            result = format(bits / (double) tera, unitFormatter) + " Tbit";
        else if(bits >= giga)
            result = format(bits / (double) giga, unitFormatter) + " Gbit";
        else if(bits >= mega)
            result = format(bits / (double) mega, unitFormatter) + " Mbit";
        else if(bits >= kilo)
            result = format(bits / (double) kilo, unitFormatter) + " Kbit";
        else
            result = bits + " bit";

        return result;
    }

    /**
     * Returns the value of a^n by multiplying a with itself n times. This is an
     * integer only operation, so only positive exponents are allowed.
     *
     * @param a base.
     * @param n exponent.
     * @return a^n.
     * @throws IllegalArgumentException if the exponent is out of range (&lt;0).
     */
    protected static long mypow(long a, long n) throws IllegalArgumentException {
        if(n < 0)
            throw new IllegalArgumentException("b can not be negative");

        long result = 1;
        for(long i = 0; i < n; ++i)
            result *= a;
        return result;
    }
}
