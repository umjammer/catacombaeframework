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

package org.catacombae.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.getLogger;


/**
 * This class wraps a java.io.RandomAccessFile (opened in read-only mode) and
 * maps its operations to the operations of ReadableRandomAccessStream.
 *
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public class ReadableFileStream implements ReadableRandomAccessStream, AbstractFileStream {

    private static final Logger logger = getLogger(ReadableFileStream.class.getName());

    protected final RandomAccessFile raf;
    private final String openPath;

    public ReadableFileStream(String filename) {
        this(new File(filename));
    }

    public ReadableFileStream(File file) {
        this(file, "r");
    }

    public ReadableFileStream(RandomAccessFile raf, String openPath) {
        logger.log(Level.TRACE, "enter: {}", raf);

        try {
            if (raf == null)
                throw new IllegalArgumentException("raf may NOT be null");
            this.raf = raf;
            this.openPath = openPath;
        } finally {
            logger.log(Level.TRACE, "leave");
        }
    }

    protected ReadableFileStream(String filename, String mode) {
        this(new File(filename), mode);
    }

    protected ReadableFileStream(File file, String mode) {
        logger.log(Level.TRACE, "enter: {}, {}", file, mode);

        try {
            this.raf = new RandomAccessFile(file, mode);
            this.openPath = file.getPath();
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            logger.log(Level.TRACE, "leave");
        }
    }

    @Override
    public void seek(long pos) {
        logger.log(Level.TRACE, "enter: {}", pos);


        try {
            raf.seek(pos);
        } catch (IOException ioe) {
            throw new RuntimeIOException("pos=" + pos + "," + ioe, ioe);
        } finally {
            logger.log(Level.TRACE, "leave: {}", pos);
        }
    }

    @Override
    public int read() {
        logger.log(Level.TRACE, "enter");

        try {
            int res = raf.read();
            logger.log(Level.TRACE, "return: {}", res);
            return res;
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            logger.log(Level.TRACE, "leave");
        }
    }

    @Override
    public int read(byte[] data) {
        logger.log(Level.TRACE, "enter: {}", data);

        try {
            int res = raf.read(data);
            logger.log(Level.TRACE, "return: {}", res);
            return res;
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            logger.log(Level.TRACE, "leave: {}", data);
        }
    }

    @Override
    public int read(byte[] data, int pos, int len) {
        logger.log(Level.TRACE, "enter: {}, {}, {}", data, pos, len);

        try {
            int res = raf.read(data, pos, len);
            logger.log(Level.TRACE, "return: {}", res);
            return res;
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            logger.log(Level.TRACE, "leave: {}, {}, {}", data, pos, len);
        }
    }

    @Override
    public byte readFully() {
        logger.log(Level.TRACE, "enter");

        try {
            byte[] data = new byte[1];
            raf.readFully(data);
            return data[0];
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            logger.log(Level.TRACE, "leave");
        }
    }

    @Override
    public void readFully(byte[] data) {
        logger.log(Level.TRACE, "enter: {}", data);

        try {
            raf.readFully(data);
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            logger.log(Level.TRACE, "leave: {}", data);
        }
    }

    @Override
    public void readFully(byte[] data, int offset, int length) {
        logger.log(Level.TRACE, "enter: {}, {}, {}", data, offset, length);

        try {
            raf.readFully(data, offset, length);
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            logger.log(Level.TRACE, "leave: {}, {}, {}", data, offset, length);
        }
    }

    @Override
    public long length() {
        logger.log(Level.TRACE, "enter");

        try {
            return raf.length();
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            logger.log(Level.TRACE, "leave");
        }
    }

    @Override
    public long getFilePointer() {
        logger.log(Level.TRACE, "enter");

        try {
            long res = raf.getFilePointer();
            logger.log(Level.TRACE, "return: {}", res);
            return res;
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            logger.log(Level.TRACE, "leave");
        }
    }

    @Override
    public void close() {
        logger.log(Level.TRACE, "enter");

        try {
            raf.close();
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        } finally {
            logger.log(Level.TRACE, "leave");
        }
    }

    @Override
    public String getOpenPath() {
        return openPath;
    }
}
