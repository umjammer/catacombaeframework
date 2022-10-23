/*-
 * Copyright (C) 2009-2021 Erik Larsson
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

import java.util.LinkedList;
import java.util.logging.Logger;

import org.catacombae.util.Util;


/**
 * Common logging class for Catacombae framework.
 *
 * @author <a href="https://catacombae.org" target="_top">Erik Larsson</a>
 */
class IOLog {

    private static final Logger logger = Logger.getLogger("org.catacombae");

    /** The default setting for the 'trace' log level. */
    public static boolean defaultTrace = false;

    /** The default setting for the 'debug' log level. */
    public static boolean defaultDebug = false;

    /** The current setting of the 'trace' log level for this instance. */
    public boolean trace = defaultTrace;

    /** The current setting of the 'debug' log level for this instance. */
    public boolean debug = defaultDebug;

    private IOLog(Class cls) {
        final LinkedList<String> debugLogProperties = new LinkedList<>();
        final LinkedList<String> traceLogProperties = new LinkedList<>();
        String component = null;

        for (String s : cls.getCanonicalName().split("\\.")) {
            component = ((component != null) ? component + "." : "") + s;
            debugLogProperties.add(component + ".debug");
            traceLogProperties.add(component + ".trace");
        }

        this.debug = Util.booleanEnabledByProperties(this.debug,
                debugLogProperties.toArray(new String[0]));
        this.trace = Util.booleanEnabledByProperties(this.trace,
                traceLogProperties.toArray(new String[0]));
    }

    /** Emits a 'debug' level message. */
    public final void debug(String message) {
        if (debug)
            logger.fine(message);
    }

    /**
     * Free form trace level log message.
     *
     * @param msg the message to emit.
     */
    public final void trace(String msg) {
        if (trace)
            logger.finest(msg);
    }

    /**
     * Called upon method entry, and generates a trace level message starting
     * with "ENTER: ".
     *
     * @param args the method/constructor's arguments.
     */
    public final void traceEnter(Object... args) {
        if (trace) {
            final StackTraceElement ste =
                    Thread.currentThread().getStackTrace()[2];
            final String className = ste.getClassName();
            final String methodName = ste.getMethodName();

            StringBuilder sb = new StringBuilder("ENTER: ");
            sb.append(className);
            if (methodName != null)
                sb.append(".").append(methodName);
            sb.append("(");
            for (int i = 0; i < args.length; ++i) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(args[i]);
            }
            sb.append(")");

            trace(sb.toString());
        }
    }

    /**
     * Called upon method exit, and generates a trace level message starting
     * with "LEAVE: ".
     *
     * @param args the method/constructor's arguments.
     */
    public final void traceLeave(Object... args) {
        if (trace) {
            final StackTraceElement ste =
                    Thread.currentThread().getStackTrace()[2];
            final String className = ste.getClassName();
            final String methodName = ste.getMethodName();

            StringBuilder sb = new StringBuilder("LEAVE: ");
            sb.append(className);
            if (methodName != null)
                sb.append(".").append(methodName);
            sb.append("(");
            for (int i = 0; i < args.length; ++i) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(args[i]);
            }
            /*
            if(retval != null)
                sb.append("): ").append(retval);
            else*/
            sb.append(")");

            trace(sb.toString());
        }
    }

    /**
     * Called before a method returns with a value.
     *
     * @param retval the value returned.
     */
    public final void traceReturn(Object retval) {
        if (trace)
            trace("RETURN: " + retval);
    }

    /**
     * Returns an IOLog instance for a specific class.
     *
     * @param cls the class for which the instance should be valid.
     * @return an IOLog instance.
     */
    public static IOLog getInstance(Class cls) {
        return new IOLog(cls);
    }
}
