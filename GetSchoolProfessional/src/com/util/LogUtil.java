/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

/**
 * LogUtil.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Dec 7, 2017$
 * @since 1.0
 */
public class LogUtil
{
    /** Chained exception methods. */
    private static final String m_chainMethods[] =
    {
        "getLinkedException",
        "getNested"
    };
    /**
     * Renders the throwable object as a String. 
     * @param th the throwable object.
     * @return the rendered string.
     */
    public static String toString(Throwable th)
    {
        StringWriter sw = new StringWriter();
        printStackTrace(new PrintWriter(sw), th);
        return sw.getBuffer().toString();
    }

    /**
     * Prints exception stacktrace.
     * This will handle non-standard chained exceptions like JMSException.
     * @param pw the print writer object. 
     * @param th the throwable object.
     */
    protected static void printStackTrace(PrintWriter pw, Throwable th)
    {
        String[] methods = m_chainMethods;
        th.printStackTrace(pw);
        for (int n = 0; n < methods.length; n++)
        {
            try
            {
                Method m = th.getClass().getMethod(m_chainMethods[n], null);
                Throwable link = (Throwable) m.invoke(th, null);
                if (link != null && link != th)
                {
                    pw.print("Caused by: ");
                    printStackTrace(pw, link);
                }
                break;
            }
            catch (Throwable ignore)
            {
            }
        }
    }
}
