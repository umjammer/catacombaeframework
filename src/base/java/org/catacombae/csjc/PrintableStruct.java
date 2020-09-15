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

import java.io.PrintStream;

/**
 * Represents a struct which is printable, i.e. a view of its contents can be printed to a
 * java.io.PrintStream.<br>
 * The printed contents will be space-indented for readability.
 *
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public interface PrintableStruct {
    /**
     * Prints the name of the struct along with the fields of the struct to <code>ps</code>
     * prepending <code>prefix</code> to each line.
     *
     * @param ps the stream to print the contents to.
     * @param prefix the string prefix to prepend to each line (useful for indenting).
     */
    public void print(PrintStream ps, String prefix);

    /**
     * Prints <b>only the fields of the struct</b> to <code>ps</code> prepending
     * <code>prefix</code> to each line.
     *
     * @param ps the stream to print the contents to.
     * @param prefix the string prefix to prepend to each line (useful for indenting).
     */
    public void printFields(PrintStream ps, String prefix);
}
