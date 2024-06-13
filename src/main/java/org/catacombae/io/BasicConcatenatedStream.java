/*-
 * Copyright (C) 2007-2008 Erik Larsson
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

import java.io.PrintStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.getLogger;


/**
 * Common superclass of ReadableConcatenatedStream and ConcatenatedStream.
 *
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
public abstract class BasicConcatenatedStream<A extends ReadableRandomAccessStream>
        extends BasicReadableRandomAccessStream {

    private static final Logger logger = getLogger(BasicConcatenatedStream.class.getName());

    protected class Part {

        public final A file;
        public final long startOffset;
        public final long length;

        public Part(A file, long startOffset, long length) {
            this.file = file;
            this.startOffset = startOffset;
            this.length = length;
        }
    }

    protected final List<Part> parts = new ArrayList<>();
    protected long virtualFP;

    protected BasicConcatenatedStream(A firstPart, long startOffset, long length) {
        logger.log(Level.TRACE, "enter: {}, {}, {}", firstPart, startOffset, length);

        try {
            if (startOffset < 0) {
                // Negative startOffset means there is an hole segment inserted
                // before the first byte of the stream.
                Part missingPart = new Part(null, startOffset, -startOffset);
                parts.add(missingPart);
                length += startOffset;
                startOffset = 0;
            }

            Part currentPart = new Part(firstPart, startOffset, length);
            parts.add(currentPart);
            virtualFP = 0;
        } finally {
            logger.log(Level.TRACE, "leave: {}, {}, {}", firstPart, startOffset, length);
        }
    }

    private void enter(String methodName, Object... args) {
        PrintStream out = System.err;
        out.print(this.getClass().getSimpleName() + "{" +
                this.hashCode() + "}");
        if (methodName != null)
            out.print("." + methodName);
        out.print("(");
        for (int i = 0; i < args.length; ++i) {
            if (i > 0)
                out.print(", ");
            out.print(args[i].toString());
        }
        out.println(");");
    }

    private void log(String methodName, String message) {
        PrintStream out = System.err;
        out.println(this.getClass().getSimpleName() + "{" +
                this.hashCode() + "}." + methodName + ": " + message);
    }

    public void addPart(A newFile, long off, long len) {
        logger.log(Level.TRACE, "enter: {}, {}, {}", newFile, off, len);

        Part newPart = new Part(newFile, off, len);
        parts.add(newPart);

        logger.log(Level.TRACE, "leave: {}, {}, {}", newFile, off, len);
    }

    @Override
    public void seek(long pos) {
        logger.log(Level.TRACE, "enter: {}", pos);

        virtualFP = pos;

        logger.log(Level.TRACE, "leave: {}", pos);
    }

    @Override
    public int read(byte[] data, int off, int len) {
//        String METHOD_NAME = "read";
        logger.log(Level.TRACE, "enter: {}, {}, {}", data, off, len);

        logger.log(Level.DEBUG, "virtualFP=" + virtualFP);

        try {
            int bytesRead = 0;

            long bytesToSkip = virtualFP;
            int requestedPartIndex = 0;
            for (Part p : parts) {
                if (bytesToSkip < p.length) {
                    // The first byte of virtualFP is within this part.
                    break;
                }

                ++requestedPartIndex;
                bytesToSkip -= p.length;
            }

            if (requestedPartIndex >= parts.size()) {
                return -1;
            }

            while (requestedPartIndex < parts.size()) {
                Part requestedPart = parts.get(requestedPartIndex++);

                logger.log(Level.DEBUG, "requestedPartIndex = " + requestedPartIndex);
                logger.log(Level.DEBUG, "requestedPart.length = " + requestedPart.length);
                logger.log(Level.DEBUG, "requestedPart.startOffset = " + requestedPart.startOffset);

                long bytesToSkipInPart = bytesToSkip;

                logger.log(Level.DEBUG, "bytesToSkipInPart = " + bytesToSkipInPart);

                bytesToSkip = 0;

                int bytesLeftToRead = len - bytesRead;

                logger.log(Level.DEBUG, "bytesLeftToRead = " + bytesLeftToRead);

                int bytesToRead = (int) (bytesLeftToRead < requestedPart.length
                        ? bytesLeftToRead : requestedPart.length);

                logger.log(Level.DEBUG, "bytesToRead = " + bytesToRead);

                int res;
                if (requestedPart.file == null) {
                    // This is a hole, so just zero-fill.
                    Arrays.fill(data, off + bytesRead, bytesToRead, (byte) 0);
                    res = bytesToRead;
                } else {
                    logger.log(Level.DEBUG, "seeking to " + bytesToSkipInPart);

                    requestedPart.file.seek(requestedPart.startOffset + bytesToSkipInPart);

                    logger.log(Level.DEBUG, "invoking requestedPart.file.read(byte[" +
                            data.length + "], " + (off + bytesRead) + ", " + bytesToRead + ")");

                    res = requestedPart.file.read(data, off + bytesRead, bytesToRead);
                }

                logger.log(Level.DEBUG, "res = " + res);

                if (res > 0) {
                    virtualFP += res;
                    bytesRead += res;
                    if (bytesRead == len) {
                        logger.log(Level.DEBUG, "returning " + bytesRead);

                        return bytesRead;
                    } else if (bytesRead > len)
                        throw new RuntimeException("Read more than I was supposed to! This should not be possible.");
                } else {
                    if (bytesRead > 0)
                        return bytesRead;
                    else
                        return -1;
                }
            }

            logger.log(Level.TRACE, "return: {}", bytesRead);
            return bytesRead;
        } finally {
            logger.log(Level.TRACE, "leave: {}, {}, {}", data, off, len);
        }
    }

    @Override
    public long length() {
//        String METHOD_NAME = "length";
        logger.log(Level.TRACE, "enter");

        long result = 0;
        for (BasicConcatenatedStream<?>.Part p : parts)
            result += p.length;

        logger.log(Level.DEBUG, "returning " + result);
        logger.log(Level.TRACE, "return: {}", virtualFP);
        logger.log(Level.TRACE, "leave");

        return result;
    }

    @Override
    public long getFilePointer() {
//        String METHOD_NAME = "getFilePointer";
        logger.log(Level.TRACE, "enter");
        logger.log(Level.TRACE, "return: {}", virtualFP);
        logger.log(Level.TRACE, "leave");

        return virtualFP;
    }

    /** Closes all the files constituting this BasicConcatenatedStream. */
    @Override
    public void close() {
        logger.log(Level.TRACE, "enter");

        for (Part p : parts) {
            if (p.file != null) {
                p.file.close();
            }
        }

        logger.log(Level.TRACE, "leave");
    }
}
