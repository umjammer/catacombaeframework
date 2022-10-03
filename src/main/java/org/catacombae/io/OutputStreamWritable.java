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
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.catacombae.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Transforms a java.io.OutputStream into an org.catacombae.io.Writable.
 *
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public class OutputStreamWritable extends BasicWritable implements Stream {
    private final OutputStream os;

    public OutputStreamWritable(OutputStream os) {
        this.os = os;
    }
    
    /** {@inheritDoc} */
    @Override
    public void write(byte[] b, int off, int len) throws RuntimeIOException {
        try {
            os.write(b, off, len);
        } catch(IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    /** {@inheritDoc} */
    public void close() throws RuntimeIOException {
        try {
            os.close();
        } catch(IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

}
