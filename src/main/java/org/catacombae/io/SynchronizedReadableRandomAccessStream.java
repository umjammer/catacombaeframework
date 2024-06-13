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

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.HashMap;

import org.catacombae.util.Util;

import static java.lang.System.getLogger;


/**
 * This class adds concurrency safety to a random access stream. It includes a
 * seek+read atomic operation. All operations on this object is synchronized on
 * its own monitor.
 *
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public class SynchronizedReadableRandomAccessStream
        extends BasicSynchronizedReadableRandomAccessStream
        implements SynchronizedReadableRandomAccess {

    private static final Logger logger = getLogger(SynchronizedReadableRandomAccessStream.class.getName());

    private static final boolean REFERENCES_DEBUG =
            Util.booleanEnabledByProperties(logger.isLoggable(Level.DEBUG),
                    "org.catacombae.io." +
                            SynchronizedReadableRandomAccessStream.class.getSimpleName() +
                            ".references_debug");

    /** The underlying stream. */
    private final ReadableRandomAccessStream ras;
    private long refCount;
    private boolean closed = false;
    private final HashMap<Object, Reference> references =
            REFERENCES_DEBUG ? new HashMap<>() : null;

    public SynchronizedReadableRandomAccessStream(
            ReadableRandomAccessStream sourceStream) {
        this.ras = sourceStream;
        this.refCount = 1;

        if (REFERENCES_DEBUG) {
            references.put(this, new Reference(this,
                    new Exception().getStackTrace()));
        }
    }

    /**
     * Returns the backing stream for this
     * SynchronizedReadableRandomAccessStream.
     *
     * @return the backing stream for this
     * SynchronizedReadableRandomAccessStream.
     */
    public ReadableRandomAccessStream getSourceStream() {
        return ras;
    }

    @Override
    public synchronized int readFrom(long pos, byte[] b, int off, int len) throws RuntimeIOException {
        logger.log(Level.DEBUG, "SynchronizedReadableRandomAccessStream.readFrom(" + pos +
                ", byte[" + b.length + "], " + off + ", " + len + ");");

        long oldFP = getFilePointer();

        logger.log(Level.DEBUG, "  oldFP=" + oldFP);

        if (oldFP != pos) {
            logger.log(Level.DEBUG, "  seeking to " + pos + "...");

            seek(pos);
        }

        int res;

        try {
            logger.log(Level.DEBUG, "  Reading " + len + " bytes...");

            res = read(b, off, len);

            logger.log(Level.DEBUG, "    read " + res + " bytes.");
        } finally {
            logger.log(Level.DEBUG, "  seeking to " + oldFP + "...");

            seek(oldFP); // Reset file pointer to previous position
        }

        logger.log(Level.DEBUG, "  returning " + res + ".");

        return res;
    }

    @Override
    public synchronized long skipFrom(long pos, long length) throws RuntimeIOException {
        long streamLength = length();
        long newPos = pos + length;

        long res;
        if (newPos > streamLength) {
//            seek(streamLength);
            res = streamLength - pos;
        } else {
//            seek(newPos);
            res = length;
        }

        return res;
    }

    @Override
    public synchronized long remainingLength() throws RuntimeIOException {
        return length() - getFilePointer();
    }

    @Override
    public synchronized void close() throws RuntimeIOException {
        logger.log(Level.DEBUG, SynchronizedReadableRandomAccessStream.class.getName() +
                "@" + Util.toHexStringBE(hashCode()) + ".close(): Called " +
                "from " + new Exception().getStackTrace()[1] + ".");

        if (closed) {
            throw new IllegalStateException("Already closed.");
        }

        if (REFERENCES_DEBUG) {
            if (references.remove(this) == null) {
                throw new IllegalStateException("Own reference not found!");
            }
        }

        --refCount;
        tryCloseSource();
        closed = true;
    }

    private void tryCloseSource() {
        if (refCount == 0) {
            ras.close();
        }
    }

    @Override
    public synchronized long getFilePointer() throws RuntimeIOException {
        return ras.getFilePointer();
    }

    @Override
    public synchronized long length() throws RuntimeIOException {
        return ras.length();
    }

    @Override
    public synchronized int read() throws RuntimeIOException {
        return ras.read();
    }

    @Override
    public synchronized int read(byte[] b) throws RuntimeIOException {
        return ras.read(b);
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) throws RuntimeIOException {
        logger.log(Level.DEBUG, "SynchronizedReadableRandomAccessStream.read(" +
                "byte[" + b.length + "], " + off + ", " + len + ");");
        logger.log(Level.DEBUG, "  ras=" + ras);

        return ras.read(b, off, len);
    }

    @Override
    public synchronized void seek(long pos) throws RuntimeIOException {
        ras.seek(pos);
    }

    @Override
    public synchronized void addReference(Object referrer) {
        logger.log(Level.DEBUG, this + ": Reference added (" + refCount + " " +
                "-> " + (refCount + 1) + ") by " + referrer + ".");

        if (!closed) {
            if (REFERENCES_DEBUG) {
                if (references.get(referrer) != null) {
                    throw new IllegalStateException("Only one reference per referrer is allowed.");
                }

                references.put(referrer, new Reference(referrer, new Exception().getStackTrace()));
            }

            ++refCount;
        } else
            throw new RuntimeIOException("Stream is closed!");
    }

    @Override
    public synchronized void removeReference(Object referrer) {
        if ((closed && refCount == 0) || (!closed && refCount == 1)) {
            throw new RuntimeException("No references!");
        }

        logger.log(Level.DEBUG, this + ": Reference removed (" + refCount + " " +
                "-> " + (refCount - 1) + ") by " + referrer + ".");

        if (REFERENCES_DEBUG) {
            if (references.remove(referrer) == null) {
                throw new RuntimeException("Reference not found!");
            }
        }

        --refCount;

        tryCloseSource();
    }

    @Override
    protected synchronized void finalize() throws Throwable {
        try {
            if (refCount != 0) {
                logger.log(Level.DEBUG, "[WARNING] " + this + " is garbage " +
                        "collected with " + refCount + " remaining references" +
                        (REFERENCES_DEBUG ? ":" : "."));
                if (REFERENCES_DEBUG) {
                    for (Reference r : references.values()) {
                        logger.log(Level.DEBUG, r.referrer);
                        for (StackTraceElement ste : r.stackTrace) {
                            logger.log(Level.DEBUG, "\t" + ste);
                        }
                    }
                }
            }
        } finally {
            super.finalize();
        }
    }

    private static class Reference {

        final Object referrer;
        final StackTraceElement[] stackTrace;

        public Reference(Object referrer, StackTraceElement[] stackTrace) {
            this.referrer = referrer;
            this.stackTrace = stackTrace;
        }
    }
}
