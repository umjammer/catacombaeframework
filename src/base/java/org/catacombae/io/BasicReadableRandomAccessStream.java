/*-
 * Copyright (C) 2008 Erik Larsson
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catacombae.io;

/**
 * Basic implementation of many features of ReadableRandomAccessStream, to allow the subclasser to
 * only implement the essential methods needed to support a ReadableRandomAccessStream.
 * 
 * @author <a href="http://hem.bredband.net/catacombae">Erik Larsson</a>
 */
public abstract class BasicReadableRandomAccessStream implements ReadableRandomAccessStream {
    /**
     * Empty constructor (there is no state maintained in this class).
     */
    protected BasicReadableRandomAccessStream() { }
    
    /** {@inheritDoc} */
    public abstract void close() throws RuntimeIOException;

    /** {@inheritDoc} */
    public abstract void seek(long pos) throws RuntimeIOException;

    /** {@inheritDoc} */
    public abstract long length() throws RuntimeIOException;

    /** {@inheritDoc} */
    public abstract long getFilePointer() throws RuntimeIOException;

    /** {@inheritDoc} */
    public int read() throws RuntimeIOException {
        byte[] res = new byte[1];
        if(read(res, 0, 1) == 1)
            return res[0] & 0xFF;
        else
            return -1;
    }

    /** {@inheritDoc} */
    public int read(byte[] data) throws RuntimeIOException {
        return read(data, 0, data.length);
    }

    /** {@inheritDoc} */
    public abstract int read(byte[] data, int pos, int len) throws RuntimeIOException;

    /** {@inheritDoc} */
    public void readFully(byte[] data) throws RuntimeIOException {
	readFully(data, 0, data.length);
    }

    /** {@inheritDoc} */
    public void readFully(byte[] data, int offset, int length) throws RuntimeIOException {
        if(length < 0)
            throw new IllegalArgumentException("length is negative: " + length);
	int bytesRead = 0;
	while(bytesRead < length) {
	    int curBytesRead = read(data, offset+bytesRead, length-bytesRead);
	    if(curBytesRead > 0) bytesRead += curBytesRead;
	    else 
		throw new RuntimeIOException("Couldn't read the entire length.");
	}
    }
}
