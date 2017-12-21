/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.tool.util;

/**
 * Filter.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Dec 15, 2017$
 * @since 1.0
 */
public class Filter
{
    public static String DeleteSymbol(String content)
    {
        content = content.replaceAll("\r|\n", "");
        content = content.replaceAll("  ", "");
        content = content.replaceAll("&ensp;", "");
        return content;
    }
}
