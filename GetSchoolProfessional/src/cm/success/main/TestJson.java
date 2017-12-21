/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package cm.success.main;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.sf.json.JSONArray;

/**
 * TestJson.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Dec 19, 2017$
 * @since 1.0
 */
public class TestJson
{

    @Test
    public static void testJsonStrToJSON()
    {
        JSONArray jsonArray = JSONArray.fromObject( "['json','is','easy']" );  
        System.out.println( jsonArray );  
        
    }

    public static void testListToJSON()
    {
        List list = new ArrayList();  
        list.add( "first" );  
        list.add( "second" );  
        JSONArray jsonArray = JSONArray.fromObject( list );  
        System.out.println( jsonArray );  
        // prints ["first","second"]  
    }
    
    public static void main(String[] args)
    {
        testJsonStrToJSON();
        testListToJSON();
    }
}
