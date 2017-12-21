/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * IOUtil.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Dec 13, 2017$
 * @since 1.0
 */
public class IOUtil
{
    public static void close(Object obj)
    {
        if (obj == null)
            return;
        
        try
        {
            if (obj instanceof Closeable)
                ((Closeable) obj).close();
            else
                throw new IllegalArgumentException("Unable to close " + obj.getClass());
        }
        catch (IOException e)
        {
        }
    }
}
