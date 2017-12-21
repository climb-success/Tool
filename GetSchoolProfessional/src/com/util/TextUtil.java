/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.util;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.WeakHashMap;

/**
 * TextUtil.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Nov 19, 2017$
 * @since 1.0
 */
public class TextUtil
{
    /** Path where message files are stored. */
    public static String MESSAGES = "messages/";

    private static Map m_bundles = new HashMap();

    static char[] m_hex =
    {
        '0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' ,
        '8' , '9' , 'A' , 'B' , 'C' , 'D' , 'E' , 'F'
    };

    
    static char[] m_alphanumeric = 
    {
        '0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' ,
        '8' , '9' , 'A' , 'B' , 'C' , 'D' , 'E' , 'F' ,
        'G' , 'H' , 'I' , 'J' , 'K' , 'L' , 'M' , 'N' ,
        'O' , 'P' , 'Q' , 'R' , 'S' , 'T' , 'U' , 'V' ,
        'W' , 'X' , 'Y' , 'Z' 
    };
    
    static char[] m_alphanumeric2 = 
    {
        '0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' ,
        '8' , '9' , 'A' , 'B' , 'C' , 'D' , 'E' , 'F' ,
        'G' , 'H' , 'I' , 'J' , 'K' , 'L' , 'M' , 'N' ,
        'O' , 'P' , 'Q' , 'R' , 'S' , 'T' , 'U' , 'V' ,
        'W' , 'X' , 'Y' , 'Z' , 'a' , 'b' , 'c' , 'd' ,
        'e' , 'f' , 'g' , 'h' , 'i' , 'j' , 'k' , 'l' ,
        'm' , 'n' , 'o' , 'p' , 'q' , 'r' , 's' , 't' ,
        'u' , 'v' , 'w' , 'x' , 'y' , 'z'
    };
    
    static char[] m_alphanumeric3 = 
    {
        '2' , '3' , '4' , '5' , '6' , '7' , '8' ,
        '9' , 'A' , 'B' , 'C' , 'D' , 'E' , 'F' ,
        'G' , 'H' , 'J' , 'K' , 'L' , 'M' , 'N' ,
        'P' , 'Q' , 'R' , 'S' , 'T' , 'U' , 'V' ,
        'W' , 'X' , 'Y' , 'Z' 
    };
    
    private static Random random = new Random();
    
    /**
     * Not meant to be instantiated.
     */
    private TextUtil()
    {
    }
    
    /**
     * Tests to see if a string is empty.
     * @param s the string to test.
     * @return true if string is null or blank.
     */
    public static boolean isEmpty(String s)
    {
        return (s == null || s.length() == 0 || s.trim().length() == 0);
    }
    
    /**
     * Pads a string to the desired length.
     * @param s the string.
     * @param len the desired length.
     * @param ch the character used for padding
     * @return the padded string.
     */
    public static String lpad(String s, int len, char ch)
    {
        if (s == null || s.length() >= len)
            return s;
        
        StringBuffer buf = new StringBuffer(len);
        for (len -= s.length(); len > 0; len--)
            buf.append(ch);
        
        buf.append(s);
        return buf.toString();
    }
    
    /**
     * Pads a string to the desired length.
     * @param s the string.
     * @param len the desired length.
     * @param ch the character used for padding
     * @return the padded string.
     */
    public static String rpad(String s, int len, char ch)
    {
        if (s == null || s.length() >= len)
            return s;
        
        StringBuffer buf = new StringBuffer(len);
        buf.append(s);
        for (len -= s.length(); len > 0; len--)
            buf.append(ch);
        
        return buf.toString();
    }
    
    /**
     * Left trims a string.
     * @param s the string to ltrim.
     * @return a trimmed string.
     */
    public static String ltrim(String s)
    {
        if (s == null)
            return null;
        
        for (int k = 0; k < s.length(); k++)
        {
            if (!Character.isWhitespace(s.charAt(k)))
                return (k == 0 ? s : s.substring(k));
        }
        return "";
    }

    /**
     * Right trims a string.
     * @param s the string to rtrim.
     * @return a trimmed string.
     */
    public static String rtrim(String s)
    {
        if (s == null)
            return null;
        
        for (int k = s.length()-1; k >= 0; k--)
        {
            if (!Character.isWhitespace(s.charAt(k)))
                return (k == s.length() - 1 ? s : s.substring(0, k + 1));
        }
        return "";
    }
    
    /**
     * Trims a string.
     * This is equivalent (s == null ? null : s.trim()).
     * @param s the string to ltrim.
     * @return a trimmed string.
     */
    public static String trim(String s)
    {
        return (s == null ? null : s.trim());
    }
    
    /**
     * Trims the string array.
     * @param ss the strings
     * @return the trimmed string array
     */
    public static String[] trim(String[] ss)
    {
        if (ss == null)
            return ss;
        
        for (int i = 0; i < ss.length; i++)
        {
            ss[i] = TextUtil.trim(ss[i]);
        }
        return ss;
    }
    
    /**
     * Expands tokenized string.
     * This is equivalent to {@link #expand(String, Map) expand(s, null)}.
     * @param s the string to expand.
     * @return the expanded string.
     */
    public static String expand(String s)
    {
        return expand(s, null);
    }

    /**
     * Expands tokenized string.
     * It will use System.getProperty() if it cannot find a match in the map.
     * @param s the string to expand.
     * @param map the property map.
     * @return the expanded string.
     */
    public static String expand(String s, Map map)
    {
        return TextUtil.expand(s, "${", "}", map);
    }
    
    /**
     * Expands tokenized string.
     * It will use System.getProperty() if it cannot find a match in the map.
     * @param s the string to expand.
     * @param map the property map.
     * @return the expanded string.
     */
    public static String expand(String s, String begin, String end, Map map)
    {
        return expand(s, begin, end, map, false);
    }

    /**
     * Expands tokenized string.
     * It will use System.getProperty() if it cannot find a match in the map.
     * @param s the string to expand.
     * @param map the property map.
     * @param emptyIfMissing replace the variable with empty if missing.
     * @return the expanded string.
     */
    public static String expand(String s, String begin, String end, Map map,
            boolean emptyIfMissing)
    {
        if (s == null || s.length() == 0)
            return s;

        StringBuffer buf = new StringBuffer();
        int k1 = 0;
        int k2;
        while ((k2 = s.indexOf(begin, k1)) >= 0)
        {
            buf.append(s.substring(k1, k2));
            if ((k1 = s.indexOf(end, k2 + begin.length())) < 0)
            {
                buf.append(s.substring(k2));
                break;
            }

            String f = s.substring(k2 + begin.length(), k1);
            Object v = (map == null ? null : map.get(f));
            if (v == null)
            {
                if (!emptyIfMissing)
                    buf.append(begin).append(f).append(end);
            }
            else
            {
                buf.append(v);
            }
            k1++;
        }

        if (k1 >= 0 && k1 < s.length())
            buf.append(s.substring(k1));
        
        return buf.toString();
    }
    
    /**
     * Expands tokenized strings.
     * Equivalent to {@link #expand(Properties, Map) expand(props, null)}.
     * @param props the properties to expand.
     * @return the expanded properties.
     */
    public static Properties expand(Properties props)
    {
        return expand(props, null);
    }

    /**
     * Expands tokenized strings.
     * It calls {@link #expand(String, Map) expand(String, null)} for each
     * property.
     * @param props the properties to expand.
     * @param map the token value mappings.
     * @return the expanded properties.
     */
    public static Properties expand(Properties props, Map map)
    {
        if (props == null || props.size() == 0)
            return props;
        
        Iterator i = props.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry e = (Map.Entry) i.next();
            Object val = e.getValue();
            if (val != null)
                e.setValue(expand(val.toString(), map));
        }
        return props;
    }

    /**
     * Translates key to localized message.
     * @param baseName the base name (fully qualified package/class name).
     * @param key the message key.
     * @return the translated message.
     * @see TextUtil#lookup(String, String, Object, Locale, ClassLoader)
     */
    public static String lookup(String baseName, String key)
    {
        return lookup(baseName, key, null, null, null);
    }

    /**
     * Translates key to localized message.
     * @param baseName the base name (fully qualified package/class name).
     * @param key the message key.
     * @param args the message parameters.
     * @return the translated message.
     * @see TextUtil#lookup(String, String, Object, Locale, ClassLoader)
     */
    public static String lookup(String baseName, String key, Object args)
    {
        return lookup(baseName, key, args, null, null);
    }

    /**
     * Translates key to localized message.
     * @param baseName the base name (fully qualified package/class name).
     * @param key the message key.
     * @param args the message parameters.
     * @param locale the locale.
     * @return the translated message.
     * @see TextUtil#lookup(String, String, Object, Locale, ClassLoader)
     */
    public static String lookup(String baseName, String key, Object args,
                                Locale locale)
    {
        return lookup(baseName, key, args, locale, null);
    }

    /**
     * Translates key to localized message.
     * <P>It first calls
     * {@link ResourceBundle#getBundle(String, Locale, ClassLoader)
     * ResourceBundle.getBundle(baseName, locale, cl)} and if it fails, it will
     * search the "${SAVANNA_HOME}/messages" folder for:
     * <UL>
     * <LI>baseName + "_" + language1 + "_" + country1 + "_" + variant1 + ".properties"</LI> 
     * <LI>baseName + "_" + language1 + "_" + country1  + ".properties"</LI>
     * <LI>baseName + "_" + language1 + ".properties"</LI>
     * <LI>baseName + "_" + language2 + "_" + country2 + "_" + variant2 + ".properties"</LI> 
     * <LI>baseName + "_" + language2 + "_" + country2  + ".properties"</LI>
     * <LI>baseName + "_" + language2 + .properties"</LI> 
     * <LI>baseName + .properties"</LI>
     * </UL>
     * where language1, country1, variant1 is the specific locale and language2,
     * country2, variant2 is the default locale.
     * @param baseName the base name (fully qualified package/class name).
     * @param key the message key (e.g. error.load.file).
     * @param args the message parameters (single object or array).
     * @param locale the locale.
     * @param cl the classloader.
     * @return the translated message.
     */
    public static String lookup(String baseName, String key, Object args,
                                Locale locale, ClassLoader cl)
    {
        // Empty key
        if (key == null)
            return null;
        
        // Default to common messages
        if (baseName == null)
            baseName = "messages.savanna.common";
        
        ResourceBundle bundle;
        Map bundles;
        synchronized(m_bundles)
        {
            if ((bundles = (Map) m_bundles.get(locale)) == null)
                m_bundles.put(locale, bundles = new WeakHashMap());
        }

        synchronized(bundles)
        {
            if ((bundle = (ResourceBundle) bundles.get(baseName)) == null)
            {
                // Load new bundle
                try
                {
                    if (cl == null)
                        cl = Thread.currentThread().getContextClassLoader();

                    // Try ${SAVANNA_HOME}/messages first
                    File path;
                    if (baseName.startsWith("messages."))
                        path = new File(expand("${SAVANNA_HOME}"));
                    else
                        path = new File(expand("${SAVANNA_HOME}/messages"));
                    
                    if (path.exists())
                        cl = new URLClassLoader(new URL[]{path.toURI().toURL()}, cl);
                
                    bundle = ResourceBundle.getBundle(baseName, locale, cl);
                }
                catch (Throwable th)
                {
                    return key;
                }
                bundles.put(baseName, bundle);
            }
        }
        
        // Get message from resource bundle
        try
        {
            String msg = bundle.getString(key);
            if (msg == null)
                return key;
            
            if (args == null)
                return msg;
            
            if (!args.getClass().isArray())
                args = new Object[]{args};
            
            return MessageFormat.format(msg, (Object[]) args);
        }
        catch (Throwable th)
        {
            return key;
        }
    }
    
    /**
     * Replaces strings with another string.
     * This method will work for JRE versions before 1.4.
     * @param s the original string.
     * @param replace the string to replace.
     * @param with the string to replace with.
     * @return the replaced string.
     */
    public static String replace(String s, String replace, String with)
    {
        if (s == null || s.length() == 0 ||
            replace == null || replace.length() == 0 || with == null ||
            replace.equals(with))
            return s;
        
        StringBuffer buf = new StringBuffer();
        int k1 = 0;
        int k2;
        while ((k2 = s.indexOf(replace, k1)) >= 0)
        {
            buf.append(s.substring(k1, k2));
            buf.append(with);
            k1 = k2 + replace.length();
        }

        if (k1 < s.length())
            buf.append(s.substring(k1));
        
        return buf.toString();
    }
    
    /**
     * Replaces the tokens with objects.
     * @param s the string
     * @param token the place holder
     * @param objects the objects
     * @return the new string replaced
     */
    public static String replace(String s, String token, Object[] objects)
    {
        if (isEmpty(s) || isEmpty(token) || objects == null || objects.length == 0)
            return s;
        
        StringBuffer buf = new StringBuffer();
        int k1 = 0;
        int k2;
        int i = 0;
        while ((k2 = s.indexOf(token, k1)) >= 0 && i < objects.length)
        {
            buf.append(s.substring(k1, k2));
            buf.append(objects[i++]);
            k1 = k2 + token.length();
        }

        if (k1 < s.length())
            buf.append(s.substring(k1));
        
        return buf.toString();
        
    }
    
    /**
     * Counts the token appearing in the string.
     * @param s the string
     * @param token the token
     * @return the count
     */
    public static int count(String s, String token)
    {
        if (isEmpty(s) || isEmpty(token))
            return 0;
        
        int count = 0;
        int k = 0;
        while ((k = s.indexOf(token, k)) >= 0)
        {
            count ++;
            k += token.length();
        }
        return count;
    }
    
    /**
     * Removes all characters except letters.
     * @param s the string to convert.
     * @return the string containing only letters.  
     */
    public static String getLetters(String s)
    {
        if (s == null || s.length() == 0)
            return s;
        
        // Removes spaces and dashes
        StringBuffer buf = new StringBuffer();
        int len = s.length();
        for (int n = 0; n < len; n++)
        {
            char c = s.charAt(n);
            if (Character.isLetter(c))
                buf.append(c);
        }
        return buf.toString();
    }
    
    /**
     * Removes all characters except number digits.
     * @param s the string to convert.
     * @return the string containing only number digits.  
     */
    public static String getDigits(String s)
    {
        if (s == null || s.length() == 0)
            return s;
        
        // Removes spaces and dashes
        StringBuffer buf = new StringBuffer();
        int len = s.length();
        for (int n = 0; n < len; n++)
        {
            char c = s.charAt(n);
            if (Character.isDigit(c))
                buf.append(c);
        }
        return buf.toString();
    }
    
    /**
     * get digits that split by the split char passed in
     * @param s
     * @param splitChar
     * @return String
     */
    public static String getDigits(String s, String splitChar)
    {
        if(isEmpty(s))
            return s;
        
        StringBuffer buf = new StringBuffer();
        int len = s.length();
        boolean noPreDigit = false,firstDigit=true;
        for(int n=0; n < len; n++)
        {
            char c = s.charAt(n);
            if(Character.isDigit(c))
            {
                if(noPreDigit && !firstDigit)
                {
                    buf.append(splitChar);
                } 
                buf.append(c);
                noPreDigit = false;
                firstDigit = false;
            }
            else
            {
                noPreDigit = true;
            }
        }
        
        return buf.toString();
    }
    
    /**
     * Removes all characters except number digits or letters.
     * @param s the string to convert.
     * @return the string containing only number digits or letters.  
     */
    public static String getLettersOrDigits(String s)
    {
        if (s == null || s.length() == 0)
            return s;
        
        // Removes spaces and dashes
        StringBuffer buf = new StringBuffer();
        int len = s.length();
        for (int n = 0; n < len; n++)
        {
            char c = s.charAt(n);
            if (Character.isLetterOrDigit(c))
                buf.append(c);
        }
        return buf.toString();
    }
    
    /**
     * Capitalize the string.
     * @param s the string
     * @return the string with first letter titlecase
     */
    public static String capitalize(String s)
    {
        if (isEmpty(s))
            return s;
        
        StringBuffer buf = new StringBuffer(s);
        buf.setCharAt(0, Character.toTitleCase(s.charAt(0)));
        return buf.toString();
    }

    /**
     * Uncaptalize the string.
     * @param s the string
     * @return the string with first letter lowercase
     */
    public static String uncaptalize(String s)
    {
        if (isEmpty(s))
            return s;
        
        StringBuffer buf = new StringBuffer(s);
        buf.setCharAt(0, Character.toLowerCase(s.charAt(0)));
        return buf.toString();
    }
    
    /**
     * Converts the string to java bean property name style, uncaptalized, no underline.
     * @param s the string
     * @return the string in java bean property name style
     */
    public static String toPropertyStyle(String s)
    {
        if (isEmpty(s))
            return s;

        StringBuffer buf = new StringBuffer();
        buf.append(Character.toLowerCase(s.charAt(0)));
        boolean uppercase = false;
        for (int i = 1; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (c == '_')
            {
                uppercase = true;
            }
            else
            {
                buf.append(uppercase ? Character.toUpperCase(c) : Character.toLowerCase(c));
                uppercase = false;
            }
        }
        return buf.toString();
    }
    
    /**
     * Describes a property name or column name (db) in human readable way.
     * @param fieldName the property name or column name
     * @param captalized captalize the first letter of any word
     * @return the name
     */
    public static String describe(String fieldName)
    {
        StringBuffer buf = new StringBuffer();
        boolean uppercase = false;
        for (int i = 0; i < fieldName.length(); i++)
        {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c))
            {
                if (!uppercase)
                    buf.append(' ');
                buf.append(Character.toLowerCase(c));
                uppercase = true;
            }
            else if (c == '_')
            {
                buf.append(' ');
                uppercase = false;
            }
            else
            {
                buf.append(c);
                uppercase = false;
            }
        }
        return buf.toString();
    }
    
    /**
     * Splits a string into tokens.
     * @param s the string to split.
     * @param sep the string separator.
     * @return the tokenized strings.
     */
    public static String[] split(String s, String sep)
    {
        if (s == null)
            return new String[0];
        
        if (s.trim().length() == 0 || sep == null || sep.length() == 0)
            return new String[]{s};
        
        List list = new ArrayList();
        int k1 = 0;
        int k2;
        while ((k2 = s.indexOf(sep, k1)) >= 0)
        {
            list.add(s.substring(k1, k2));
            k1 = k2 + 1;
        }
        list.add(s.substring(k1));
        
        return (String[]) list.toArray(new String[list.size()]);
    }
    
    /**
     * Joins a split string array back to a string.
     * @param list the string array.
     * @param sep the string separator.
     * @return the joined string.
     */
    public static String join(Object[] list, String sep)
    {
        if (list == null)
            return null;
        
        if (sep == null)
            sep = "";
        
        StringBuffer buf = new StringBuffer();
        if (list.length > 0)
            buf.append(list[0]);
        
        for (int n = 1; n < list.length; n++)
            buf.append(sep).append(list[n]);
        
        return buf.toString();
    }
    
    /**
     * Returns true if string is an integer.
     * It recognizes 9999, -9, and +9.
     * @param s the string to test.
     * @return true if string is an integer.
     */
    public static boolean isInteger(String s)
    {
        if (s == null || s.length() == 0)
            return false;
        
        //integer regular expression
        String regex = "[-|+]?\\d+";
        if (!s.matches(regex))
            return false;
        
        if (s.startsWith("+"))
            s = s.substring(1);
        
        //try convert the string to an int
        //if it can not be converted, then return false
        try
        {
            Integer.parseInt(s);
        }catch(Exception e)
        {
            return false;
        }
        
        return true;
    }
    
    public static boolean isNumeric(String s)
    {
        if (s == null || s.length() == 0)
            return false;
        
        //integer regular expression
        String regex = "[-|+]?\\d+";
        return s.matches(regex);
    }
    
    /**
     * Performs pattern matching.
     * The pattern can contain wildcard characters:
     * <LI>* to match all.</LI>
     * <LI>? to match any character.</LI>
     * @param pattern the pattern.
     * @param string the string to match.
     * @return true if matches.
     */
    public static boolean matches(String pattern, String string)
    {
        if (pattern == null)
            return (string == null);
        
        if (string == null)
            return false;
        
        if (pattern.length() == 0)
            return (string.length() == 0);

        return matches(pattern, string, 0, 0);
    }
    
    /**
     * Performs pattern matching.
     * @param pattern the pattern.
     * @param string the string to match.
     * @param p the current pattern index.
     * @param s the current string index.
     * @return true if matches.
     */
    private static boolean matches(String pattern, String string, int p, int s)
    {
        int pcount = pattern.length();
        int scount = string.length();
        for (; p < pcount && s < scount; p++, s++)
        {
            char c = pattern.charAt(p);
            switch (c)
            {
            case '*':
                // Skip consecutive wildcards
                while (p < pcount && ((c=pattern.charAt(p)) == '*' || c == '?'))
                    p++;

                // Pattern ends with * so we are done!
                if (p >= pcount)
                    return true;

                for (; s < scount; s++)
                    if (matches(pattern, string, p, s))
                        return true;

                return false;
                
            case '?': // matches any single character
                break;
                
            default: // match exact single character
                if (c != string.charAt(s))
                    return false;
            }
        }

        // Skip ending wildcards
        for (char c; p < pcount && ((c=pattern.charAt(p))=='*' || c=='?'); p++);
        
        // Matches only if pattern + string have been completely consumed
        return (p == pcount && s == scount);
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring.
     * Provided for backward compability with JRE 1.3.
     * @param buf the StringBuffer.
     * @param find the string to find.
     * @return the index of the first occurence or -1 if not found.
     */
    public static int indexOf(StringBuffer buf, String find)
    {
        return indexOf(buf, find, 0);
    }
    
    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring.
     * Provided for backward compability with JRE 1.3.
     * @param buf the StringBuffer.
     * @param find the string to find.
     * @param from the starting index to search.
     * @return the index of the first occurence or -1 if not found.
     */
    public static int indexOf(StringBuffer buf, String find, int from)
    {
        if (from < 0)
            from = 0;
        
        int mlen = find.length();
        int blen = buf.length();
        int k; 
        for (int n = from; n < blen; n++)
        {
            for (k = 0; k < mlen && n + k < blen; k++)
            {
                if (buf.charAt(n + k) != find.charAt(k))
                    break;
            }
            
            // Found!
            if (k >= mlen)
                return n;
        }
        return -1;
    }
    
    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring.
     * Provided for backward compability with JRE 1.3.
     * @param buf the StringBuffer.
     * @param find the string to find.
     * @return the index of the first occurence or -1 if not found.
     */
    public static int lastIndexOf(StringBuffer buf, String find)
    {
        return lastIndexOf(buf, find, buf.length() - 1);
    }

    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring.
     * Provided for backward compability with JRE 1.3.
     * @param buf the StringBuffer.
     * @param find the string to find.
     * @param from the starting index to search from.
     * @return the index of the first occurence or -1 if not found.
     */
    public static int lastIndexOf(StringBuffer buf, String find, int from)
    {
        if (from > buf.length() - 1)
            from = buf.length() - 1;
        
        int mlen = find.length() - 1;
        int k;
        for (int n = from; n >= mlen; n--)
        {
            for (k = mlen; k >= 0; k--)
            {
                if (buf.charAt(n + k - mlen) != find.charAt(k))
                    break;
            }
            
            // Found!
            if (k < 0)
                return n;
        }
        return -1;
    }
    
    /**
     * Converts byte[] to hex string.
     * @param b the byte array.
     * @return a hex string.
     */
    public static String toHexString(byte[] b)
    {
        if (b == null)
            return null;
        
        StringBuffer buf = new StringBuffer(b.length * 2);
        for (int n = 0; n < b.length; n++)
        {
            buf.append(m_hex[(b[n] & 0xf0) >>> 4]);
            buf.append(m_hex[b[n] & 0x0f]);
        }
        return buf.toString();
    }
    
    /**
     * Converts part of byte[] to hex string.
     * @param b the byte array
     * @param offset the from index of the array to convert
     * @param len the max length
     * @return the hex string
     */
    public static String toHexString(byte[] b, int offset, int len)
    {
        if (b == null || b.length <= offset || offset < 0 || len <= 0)
            return null;

        StringBuffer buf = new StringBuffer(len * 2);
        int limit = Math.min(b.length, len + offset);
        for (int n = offset; n < limit; n++)
        {
            buf.append(m_hex[(b[n] & 0xf0) >>> 4]);
            buf.append(m_hex[b[n] & 0x0f]);
        }
        return buf.toString();
        
    }
    
    /**
     * Converts byte[] to hex string.
     * @param b the byte array.
     * @param separator the output byte separator.
     * @return a hex string.
     */
    public static String toHexString(byte[] b, char separator)
    {
        if (b == null)
            return null;
        
        StringBuffer buf = new StringBuffer(b.length * 3);
        for (int n = 0; n < b.length; n++)
        {
            buf.append(m_hex[(b[n] & 0xf0) >>> 4]);
            buf.append(m_hex[b[n] & 0x0f]);
            buf.append(separator);
        }
        return buf.toString();
    }
    
    /**
     * Converts the string to ascii escaped.
     * @param str input string
     * @return the ascii escaped string
     */
    public static String toAscii(String str)
    {
        if (TextUtil.isEmpty(str))
            return str;
        
        Charset charset = Charset.forName("UTF-16BE");
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (c < 256 && c >= 0)
            {
                buf.append(c);
            }
            else
            {
                buf.append("\\u").append(toHexString(new String(new char[] {c}).getBytes(charset)).toLowerCase());
            }
        }
        return buf.toString();
    }
    
    /**
     * Remove the last character for the input string and return the new string
     * @param input
     * @return
     */
    public static String chop( String input )
    {
    	if ( isEmpty( input ) )
    		return input;
    	
    	String result = input.substring( 0, input.length() - 1 );
    	
    	return result;
    }
    
    /**
     * Converts an object to a string.
     * This works for all objects including arrays, iterators and null values. 
     * @param obj the object.
     * @return a string.
     */
    public static String toString(Object obj)
    {
        if (obj == null)
            return "null";
        else if (obj.getClass().isArray())
        {
            StringBuffer buf = new StringBuffer();
            buf.append("[");
            int len = Array.getLength(obj);
            if (len > 0)
                buf.append(toString(Array.get(obj, 0)));
            
            for (int n = 1; n < len; n++)
            {
                buf.append(", ");
                buf.append(toString(Array.get(obj, n)));
            }
            buf.append("]");
            return buf.toString();
        }
        else if (obj instanceof Iterator)
        {
            StringBuffer buf = new StringBuffer();
            buf.append("[");
            Iterator i = (Iterator) obj;
            if (i.hasNext())
                buf.append(i.next());
            while (i.hasNext())
                buf.append(", ").append(i.next());
            buf.append("]");
            return buf.toString();
        }
        else if (obj instanceof String)
        {
            StringBuffer buf = new StringBuffer();
            buf.append('"');
            buf.append(obj);
            buf.append('"');
            return buf.toString();
        }
        return obj.toString();
    }

    /**
     * Trims the string to length.
     * @param str the string
     * @return the length to keep
     */
    public static String substring(String str, int length)
    {
        if (str == null || str.length() <= length)
            return str;
        return str.substring(0, length);
    }
    
    public static String subStringLastIndex(String str, String firstSymbol, String lastSymbol)
    {
        if(TextUtil.isEmpty(str))
            return str;
        int first = 0;
        int last = str.length();
        if(!TextUtil.isEmpty(firstSymbol) && str.lastIndexOf(firstSymbol) > 0)
            first = str.lastIndexOf(firstSymbol);
        if(!TextUtil.isEmpty(lastSymbol) && str.lastIndexOf(lastSymbol) > 0)
            last = str.lastIndexOf(lastSymbol);
        if(first <= last)
            return str.substring(first, last);
        return null;
    }
    
    /**
     * Gets the masked string.
     * Replace all characters with an 'X' except for the last number of characters which defined by unmaskLength.
     * @param oriStr the original string.
     * @param unmaskLength the last number of characters not masked
     * @return the masked string
     */
    public static String maskString(String oriStr,int unmaskLength)
    {
        if (oriStr == null || oriStr.length() <= unmaskLength)
            return oriStr;
        
        StringBuffer masked = new StringBuffer(oriStr);
        for (int n = 0; n < masked.length() - unmaskLength; n++)
            masked.setCharAt(n, 'X');

        return masked.toString();
    }
    
    /**
     * Gets the left part of the string, the returned string's length will not exceed length parameter, and the omitted parts will be represented as "...".
     */
    public static String omitString(String oriStr, int length)
    {
        if (oriStr == null || oriStr.length() <= length)
            return oriStr;
        String rtn = oriStr.substring(0,length - 3);
        return rtn + "...";
    }
    
    /**
     * check if the 2 string is same(both null or equal)
     */
    public static boolean isSame(String str1, String str2)
    {
        if (str1 == null && str2 == null) return true;
        if (str1 == null || str2 == null) return false;
        return str1.equals(str2);
    }
    
    /**
     * check if the 2 string is same(both empty or equal)
     */
    public static boolean isSameOrBothEmpty(String str1, String str2)
    {
        if (isEmpty(str1) && isEmpty(str2)) return true;
        if (str1 == null || str2 == null) return false;
        return str1.equals(str2);
    }
    
    /**
     * Merge two strings.
     * If the string is null, just ignore it
     */
    public static String merge(String str1, String str2)
    {
        return (str1==null?"":str1)+(str2==null?"":str2);
    }
    
    /**
     * Merge two strings with connector.
     * If the string is null, just ignore it and the connector.
     */
    public static String merge(String str1, String mid, String str2)
    {
        if (str1 == null) str1 = "";
        if (str2 == null) str2 = "";
        if (isEmpty(str1) || isEmpty(str2)) mid = "";
        return str1+mid+str2;
    }
    
    /**
     * Delete sep and charactors before sep, if str contains sep
     * cut("123abc789", "abc") = 789
     * @param str
     * @param sep
     * @return
     */
    public static String cut(String str, String sep)
    {
        if (isEmpty(str))
            return "";
        int index = 0;
        if ((index = str.indexOf(sep)) >= 0)
            return str.substring(index + sep.length());
        return str;
    }
    
    /**
     * Delete the sep, if str start with sep.
     * @param str
     * @param sep
     */
    public static String lcut(String str, String sep)
    {
        if(isEmpty(str))
            return "";
        if(str.startsWith(sep))
            return str.substring(sep.length(), str.length());
        return str;
    }
    
    /**
     * Delete the sep, if str ends with sep.
     * @param str
     * @param sep
     */
    public static String rcut(String str, String sep)
    {
        if(isEmpty(str))
            return "";
        if(str.endsWith(sep))
            return substring(str, str.length() - sep.length());
        return str;
    }
    
    /**
     * get the alphanumeric string with the specific length.
     * @param length the length which is 8 by default.
     */
    public static String getRandomString(int length)
    {
        if (length <= 0) length = 8;
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length; i++)
        {
            sb.append(m_alphanumeric[Math.abs(bytes[i]%36)]);
        }
        return sb.toString();
    }
    
    /**
     * get the alphanumeric string with the specific length.
     * @param length the length which is 8 by default.
     * @param easyReadOnly the flag indicate result have 0,O,I,1.
     */
    public static String getRandomString(int length, boolean easyReadOnly)
    {
        if (easyReadOnly)
        {
            if (length <= 0) length = 8;
            byte[] bytes = new byte[length];
            random.nextBytes(bytes);
            StringBuffer sb = new StringBuffer(length);
            for (int i = 0; i < length; i++)
            {
                sb.append(m_alphanumeric3[Math.abs(bytes[i]%m_alphanumeric3.length)]);
            }
            return sb.toString();
        }
        else
            return getRandomString(length);  
    }
    
    /**
     * Get the alphanumeric string including lower case chars with the specific length.
     * @param length the length which is 8 by default.
     */
    public static String getRandomString2(int length)
    {
        if (length <= 0) length = 8;
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length; i++)
        {
            sb.append(m_alphanumeric2[Math.abs(bytes[i] % m_alphanumeric2.length)]);
        }
        return sb.toString();
    }
    
    /**
     * get empty if string is null 
     * 
     * @param string
     * @return empty string if string is null, otherwise keep old value
     */
    public static String nullToEmpty(String string)
    {
        return (string == null)?"":string;
    }
    
    /**
     * get null if string is empty or null
     * @param string
     * @return null if string is null or empty, otherwise keep old value
     */
    public static String emptyToNull(String string)
    {
        return TextUtil.isEmpty(string)?null:string;
    }
    
    /**
     * Test if 2 stings equal ingore case, null safe.
     * @param string1 string 1
     * @param string2 string 2
     * @return true means equalsIgnoreCase other.
     */
    public static boolean equalsIgnoreCase(String string1, String string2)
    {
       return string1 == null ? string2 == null : string1.equalsIgnoreCase(string2);
    }
    
    /**
     * Converts a binary array to readable text using Bas64 encoding.
     * @param input the byte array to be encode.
     * @return an encoded string.
     * @see Base64#encode(byte[], int, int)
     * @since 1.0
     */
    public static String encode(byte[] input)
    {
        return (input == null ? null : Base64.encode(input, 0, input.length));
    }
    
    /**
     * Converts a binary array to readable text using Bas64 encoding.
     * @param input the byte array to be encode.
     * @param offset the starting offset to encode.
     * @param len the number of bytes to encode.
     * @return an encoded string.
     * @see Base64#encode(byte[], int, int)
     * @since 1.0
     */
    public static String encode(byte[] input, int offset, int len)
    {
        return Base64.encode(input, offset, len);
    }
}
