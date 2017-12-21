/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jboss.util.NestedRuntimeException;


/**
 * UrlUtil.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Dec 14, 2017$
 * @since 1.0
 */
public class UrlUtil
{
    /**
     * Not meant to be instantiated.
     */
    private UrlUtil()
    {
    }

    /**
     * Encodes a URL string.
     * @param url the URL string to encode.
     * @return an encoded URL string.
     * @see URLEncoder
     * @since 1.0
     */
    public static String encode(String url)
    {
        try
        {
            Class[] cargs;
            Object[] args;
            cargs = new Class[]{String.class};
            args = new Object[]{url, "utf8"};
            return (String) URLEncoder.class.getMethod("encode", cargs)
                                            .invoke(null, args);
        }
        catch (Exception ignore)
        {
            return null;
        }
    }

    /**
     * Decodes a URL string.
     * @param url the URL string to decode.
     * @return an decoded URL string.
     * @see URLDecoder
     * @since 1.0
     */
    public static String decode(String url)
    {
        try
        {
            Class[] cargs;
            Object[] args;
            cargs = new Class[]{String.class};
            args = new Object[]{url, "utf8"};
            return (String) URLDecoder.class.getMethod("decode", cargs)
                                            .invoke(null, args);
        }
        catch (Exception ignore)
        {
            return null;
        }
    }
    
    /**
     * Gets the input stream of a URL.
     * This works around URL.openConnection() issue for file protocols.  
     * @param url the URL.
     * @return an input stream.
     */
    public static InputStream getInputStream(URL url) throws IOException
    {
        return getInputStream(url, 0);
    }
    
    /**
     * Gets the input stream of a URL.
     * This works around URL.openConnection() issue for file protocols.  
     * @param url the URL.
     * @param timeout the timeout value, only for http connection, >0 to take effect
     * @return an input stream.
     */
    public static InputStream getInputStream(URL url, int timeout) throws IOException
    {
        if ("file".equals(url.getProtocol()))
        {
            return new FileInputStream(decode(url.getFile()));
        }
        else
        {
            URLConnection conn = openConnection(url, timeout);
            return conn.getInputStream();
        }
    }
    
    /**
     * Gets the input stream of a URL by username and password
     * 
     * @param url
     * @param userName
     * @param password
     * @param timeout
     * @return InputStream
     * @throws IOException
     */
    public static InputStream getInputStream(URL url, String userName, String password, int timeout) throws IOException
    {
        if("file".equals(url.getProtocol()) || 
                (userName == null && password == null))
        {
            return getInputStream(url, timeout);
        }

        URLConnection conn = openConnection(url, timeout);
        // Sets authentication token
        String auth = userName + ":" + password;
        auth = "Basic " + TextUtil.encode(auth.getBytes());
        conn.setRequestProperty("Authorization", auth);
        return conn.getInputStream();
    }

    /**
     * Open connection with timeout (http).
     */
    private static URLConnection openConnection(URL url, int timeout)
            throws IOException
    {
        URLConnection conn = url.openConnection();
        if (conn instanceof HttpURLConnection)
        {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            if (timeout > 0)
            {
                httpConn.setConnectTimeout(timeout * 1000);
                httpConn.setReadTimeout(timeout * 1000);
            }
        }
        return conn;
    }
    
    
    /**
     * Downloads the resource to the dest file path.
     * @param url the url
     * @param destPath the dest file path
     */
    public static void download(URL url, File destPath)
    {
        download(url, destPath, 0);
    }
    
    /**
     * Downloads the resource to the dest file path.
     * @param url the url
     * @param destPath the dest file path
     */
    public static void download(URL url, File destPath, int timeout)
    {
        if (!destPath.getParentFile().exists())
            destPath.getParentFile().mkdirs();
        
        InputStream is = null;
        OutputStream os = null;
        try
        {
            int bufferSize = 1024 * 32;
            is = new BufferedInputStream(getInputStream(url, timeout), bufferSize);
            os = new BufferedOutputStream(new FileOutputStream(destPath), bufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytes;
            while ((bytes = is.read(buffer)) != -1)
                os.write(buffer, 0, bytes);
        }
        catch (IOException e)
        {
            throw new NestedRuntimeException(e);
        }
        finally
        {
            IOUtil.close(is);
            IOUtil.close(os);
        }
    }
    
    /**
     * Download the resource to destination file with specific use name and password
     * 
     * @param userName user name
     * @param password password
     * 
     */
    public static void download(URL url, String userName, String password, File destPath, int timeout)
    {
        if (destPath.getParentFile() != null && 
                !destPath.getParentFile().exists())
            destPath.getParentFile().mkdirs();
        
        InputStream is = null;
        OutputStream os = null;
        try
        {
            int bufferSize = 1024 * 32;
            
            is = new BufferedInputStream(getInputStream(url, userName, password,timeout), bufferSize);
            os = new BufferedOutputStream(new FileOutputStream(destPath), bufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytes;
            while ((bytes = is.read(buffer)) != -1)
            {
                if(hasBOM(buffer))
                {
                    os.write(buffer, 3, bytes-3);
                }else
                {
                    os.write(buffer, 0, bytes);
                }
            }
        }
        catch (IOException e)
        {
            throw new NestedRuntimeException(e);
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                }
            }
            
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e)
                {
                }
            }
        }
    }

    /**
     * Gets the output stream of a URL.
     * This works around URL.openConnection() issue for file protocols.  
     * @param url the URL.
     * @return an output stream.
     */
    public static OutputStream getOutputStream(URL url) throws IOException
    {
        return getOutputStream(url, 0);
    }
    
    /**
     * Gets the output stream of a URL.
     * This works around URL.openConnection() issue for file protocols.  
     * @param url the URL.
     * @param timout the time out.
     * @return an output stream.
     */
    public static OutputStream getOutputStream(URL url, int timeout) throws IOException
    {
        if ("file".equals(url.getProtocol()))
            return new FileOutputStream(decode(url.getFile()));
        else
        {
            URLConnection conn = openConnection(url, timeout);
            conn.setDoOutput(true);
            return conn.getOutputStream();
        }
    }
    
    private static boolean hasBOM(byte[] bytes)
    {
        boolean hasBom = false;
        if(bytes.length < 3)
            return false;
        
        if(bytes[0] == (byte)0xEF && bytes[1] == (byte)0xBB 
                && bytes[2] == (byte)0xBF)
        {
            hasBom = true;
        }
        
        return hasBom;
    }
    
    /**
     * Concats the root with path. Consolidates the separator by using root's.
     * @param root the root, separator can be either / or \
     * @param path the path
     * @return the concated string
     */
    public static String concat(String root, String path)
    {
        if (TextUtil.isEmpty(root))
            return path;
        
        if (TextUtil.isEmpty(path))
            return root;
        
        char sep1, sep2;
        if (root.indexOf('\\') != -1)
        {
            sep1 = '\\';
            sep2 = '/';
        }
        else
        {
            sep1 = '/';
            sep2 = '\\';
        }
         
        path = path.replace(sep2, sep1);
        if (root.charAt(root.length() - 1) == sep1)
        {
            if (path.charAt(0) == sep1)
                return root + path.substring(1);
            else
                return root + path;
        }
        else
        {
            if (path.charAt(0) == sep1)
                return root + path;
            else
                return root + sep1 + path;
        }
    }
    
    /**
     * Converts url to file path.
     * @param url the url
     * @return the path
     */
    public static String toFilePath(String url)
    {
        return url.substring(url.indexOf("//") + 2).replaceAll("[:&?=]", "-");
    }
    
    /**
     * Get content size of url 
     * @param url
     * @param timeout
     * @return size of url content
     * @throws IOException
     */
    public static int getContentLength(String url, int timeout) throws IOException
    {
        URLConnection connection = openConnection(new URL(url), timeout);

        try
        {
            return connection.getContentLength();
        }
        finally
        {
            if (connection instanceof HttpURLConnection)
                ((HttpURLConnection)connection).disconnect();
        }
    }
}

