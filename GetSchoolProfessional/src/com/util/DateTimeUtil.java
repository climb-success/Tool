/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * DateTimeUtil.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Nov 19, 2017$
 * @since 1.0
 */
public class DateTimeUtil
{
    private static final String defaultDateTimeFormat = "MM/dd/yyyy HH:mm:ss";
    private static final String defaultDateFormat = "MM/dd/yyyy";
    public static final String UTC_FORMAT = "yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'";
    public static final String SHORT_UTC_FORMAT = "yyyy-MM-dd\'T\'HH:mm:ss.SSS";

    // Predefined date format
    /* eg. 20060910 */
    public static final String YYYYMMDD = "yyyyMMdd";
    /* eg. 2006/09/10 */
    public static final String YYYYMMDD_F = "yyyy/MM/dd";
    /* eg. 11232015*/
    public static final String MMDDYYYY = "MMddyyyy";    
    /* eg. 200609101020 */
    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
    /* eg. 20060910102035 */
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    
    /* eg. 2006-07-10 */
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HHMMSS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYY_MM_DDTHHMMSS_SSS = "yyyy-MM-dd\'T\'HH:mm:ss.SSS";
    
    /* eg. 07/07/2006 */
    public static final String MM_DD_YYYY = "MM/dd/yyyy";
    public static final String MM_DD_YYYY_HHMM = "MM/dd/yyyy HH:mm";
    public static final String MM_DD_YYYY_HHMMSS = "MM/dd/yyyy HH:mm:ss";
    public static final String MM_DD_YYYY_HHMMSS_sss = "MM/dd/yyyy HH:mm:ss.SSS";    
    public static final String MM_DD_YYYY_hhMM = "MM/dd/yyyy hh:mm";
    public static final String MM_DD_YYYY_hhMMss = "MM/dd/yyyy hh:mm:ss";
    public static final String MM_DD_YYYY_hhMM_AP = "MM/dd/yyyy hh:mm aaa";
    public static final String MM_DD_YYYY_hhMMss_AP = "MM/dd/yyyy hh:mm:ss aaa";
    
	private static final int SECONDS_IN_MINUTE = 60;
	private static final int SECONDS_IN_HOUR = 60 * SECONDS_IN_MINUTE;
	
	public static final String HOUR_PART = "hh";
    public static final String MINUTE_PART = "mm";
    public static final String SECOND_PART = "ss";
    public static final String MILLSECOND_PART = "sss";
    
    private static DateFormat getDateFormat()
    {
        return new SimpleDateFormat(defaultDateFormat);
    }

    private static DateFormat getDatetimeFormat()
    {
        return new SimpleDateFormat(defaultDateTimeFormat);
    }
    
    /**
     * Returns a date in MM/dd/YYYY format.
     * @param date the date to be formatted.
     * @return the formatted date.
     */
    public static String formatDate(Date date)
    {
        if (date == null)
            return null;
        return getDateFormat().format(date);
    }
    
    /**
     * Format date using the input formatString
     * if input formatString is wrong, can not parsed by SimpleDateFormat
     * then user defaultDateFormat
     * @param date
     * @param formatString
     * @return
     */
    public static String formatDate(Date date, String formatString)
    {
    	if (date == null)
    		return null;
    	
    	DateFormat df = null;
    	try
    	{
    		df = new SimpleDateFormat(formatString);
    	}catch(IllegalArgumentException exception)
    	{
    		df = getDateFormat();
    	}
    	
    	return df.format(date);
    }
    
    /**
     * Formats the date to UTC time with default format.
     * @param date the date
     * @return the string in UTC format @{@link #UTC_FORMAT}
     */
    public static final String formatDate2UTC(Date date)
    {
        return formatDate2UTC(date, UTC_FORMAT);
    }

    /**
     * Formats the date to UTC time with specified format.
     * @param date the date
     * @param pattern result format
     * @return the string in Specified pattern
     */
    public static final String formatDate2UTC(Date date, String pattern)
    {
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return fmt.format(date);
    }
    
    /**
     * Format a date string from one format to another format by time zone
     * @param date the date string.
     * @param timezone the time zone
     * @return the format result.
     */
    public static final String format(String date,  TimeZone timeZone)
    {
        if (date == null)
            return "";
        
        DateFormat fmt = getDateFormat();
        if (timeZone != null)
            fmt.setTimeZone(timeZone);
        return fmt.format(date);
    }
    
    /**
     * Format date using the input pattern, timeZone
     * @param date
     * @param pattern
     * @param timeZone
     * @return
     */
    public static final String formatDate(Date date, String pattern, TimeZone timeZone)
    {
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        if (timeZone != null)
            fmt.setTimeZone(timeZone);
        return fmt.format(date);
    }
    
    /**
     * Format date using the input pattern, locale, timeZone
     * @param date
     * @param pattern
     * @param timeZone
     * @return
     */
    public static final String formatDate(Date date, String pattern, Locale locale, TimeZone timeZone)
    {
        SimpleDateFormat fmt = new SimpleDateFormat(pattern, locale);
        if (timeZone != null)
            fmt.setTimeZone(timeZone);
        return fmt.format(date);
    }
    
    /**
     * Parse the date to UTC date.
     * @param date
     * @param pattern
     * @return the date
     * @throws Exception
     */
    public static final Date parseDateUTC(String date, String pattern) throws Exception
    {
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return fmt.parse(date);
    }
    
    /**
     * Converts the datetime to UTC. Keep hour, minute and all time fractions based on current time zone.
     * For example if it's 2016-4-6 11:30:00+0800 in current time zone, then just convert it to the time in 2016-4-6 11:30:00+0000. 
     * @param date the date literally in current time zone
     * @param timeZoneId the time zone id, null to use default time zone
     * @return the date literally with UTC time zone
     */
    public static Date toUTC(Date date, String timeZoneId)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (timeZoneId != null)
            cal.setTimeZone(TimeZone.getTimeZone(timeZoneId));

        Calendar ret = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        ret.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND));
        ret.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));
        return ret.getTime();
    }

    /**
     * Converts a string into a date using MM/dd/YYYY format.
     * @param date the date to be converted.
     * @return a converted date.
     */
    public static Date parseDate(String date)
    {
        return parseDate(date, (TimeZone)null);
    }
    
    /**
     * Converts a string into a date using MM/dd/YYYY format.
     * @param date the date to be converted.
     * @return a converted date.
     */
    public static Date parseDate(String date, TimeZone timezone)
    {
        if (TextUtil.isEmpty(date))
            return null;
        try
        {
            DateFormat dateFormat = getDateFormat();
            if (timezone != null)
                dateFormat.setTimeZone(timezone);
            return dateFormat.parse(date);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    /**
     * Parses date using the input formatString
     * if input formatString is wrong, can not parsed by SimpleDateFormat
     * then user defaultDateFormat
     * @param dateStr the date string
     * @param formatString the format as string
     * @return the date
     */
    public static Date parseDate(String dateStr, String formatString)
    {
        return parseDate(dateStr, formatString, Locale.getDefault());
    }
    
    /**
     * Parses date using the input formatString and time zone
     * if input formatString is wrong, can not parsed by SimpleDateFormat
     * then user defaultDateFormat
     * @param dateStr the date string
     * @param formatString the format as string
     * @param timezone the time zone
     * @return the date
     */
    public static Date parseDate(String dateStr, String formatString, TimeZone timezone)
    {
        return parseDate(dateStr, formatString, Locale.getDefault(), timezone);
    }

    /**
     * Parses date using the input formatString
     * if input formatString is wrong, can not parsed by SimpleDateFormat
     * then user defaultDateFormat
     * @param dateStr the date string
     * @param formatString the format as string
     * @return the date
     */
    public static Date parseDate(String dateStr, String formatString, Locale locale)
    {
        return parseDate(dateStr, formatString, locale, null);
    }
    
    /**
     * Parses date using the input formatString and timezone
     * if input formatString is wrong, can not parsed by SimpleDateFormat
     * then user defaultDateFormat
     * @param dateStr the date string
     * @param formatString the format as string
     * @param timezone the time zone
     * @return the date
     */
    public static Date parseDate(String dateStr, String formatString, Locale locale, TimeZone timezone)
    {
        if (dateStr == null)
            return null;
        
        DateFormat df = null;
        try
        {
            df = new SimpleDateFormat(formatString, locale);
        }
        catch(IllegalArgumentException exception)
        {
            df = getDateFormat();
        }
        
        try
        {
            if (timezone != null)
                df.setTimeZone(timezone);
            return df.parse(dateStr);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    /**
     * Format date time with default format:MM/dd/yy HH:mm:ss.
     * @param date the date.
     * @return the date time string.
     */
    public static String formatDateTime(Date date)
    {
        if (date == null)
            return null;
        return getDatetimeFormat().format(date);
    }

    /**
     * Parse date time with default format:MM/dd/yy HH:mm:ss.
     * @param datetime the date time expression.
     * @return the parsed date.
     */
    public static Date parseDateTime(String datetime)
    {
        if (TextUtil.isEmpty(datetime))
            return null;
        try
        {
            return getDatetimeFormat().parse(datetime);
        }
        catch (ParseException e)
        {
            return null;
        }

    }

    /**
     * Format date to formatted date string.
     * @param date the date.
     * @param formatString the format string.
     * @return the formatted date string.
     */
    public static String date2String(Date date, String formatString)
    {
        try
        {
            DateFormat format = new SimpleDateFormat(formatString);
            return format.format(date);
        }
        catch (Exception e)
        {
        }
        return null;
    }

    /**
     * Zero the date to 0 o'clock.
     * @param date the date
     * @return the date at 0 o'clock
     */
    public static Date zeroTime(Date date)
    {
        if (date == null)
            return null;
        
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(date);
        Calendar cal = Calendar.getInstance();;
        cal.clear();
        cal.set(calFrom.get(Calendar.YEAR), calFrom.get(Calendar.MONTH),
                calFrom.get(Calendar.DATE));
        return cal.getTime();
    }

    /**
     * Zero the minutes to align with hour.
     * @param date the date
     * @return the date time aligned with hour
     */
    public static Date zeroMinutes(Date date)
    {
        if (date == null)
            return null;
        
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(date);
        calFrom.set(Calendar.MINUTE, 0);
        calFrom.set(Calendar.SECOND, 0);
        calFrom.set(Calendar.MILLISECOND, 0);
        return calFrom.getTime();
    }

    /**
     * Zero the minutes to align with minute.
     * @param date the date
     * @return the date time aligned with minute
     */
    public static Date zeroSeconds(Date date)
    {
        if (date == null)
            return null;
        
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(date);
        calFrom.set(Calendar.SECOND, 0);
        calFrom.set(Calendar.MILLISECOND, 0);
        return calFrom.getTime();
    }
    
    /**
     * Gets the date by year month and date.
     * @param year the year
     * @param month 0-based
     * @param date 1-based
     * @return the date at 12am
     */
    public static Date getDate(int year, int month, int date)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis((new Date()).getTime());
        cal.clear();
        cal.set(year, month, date);
        return cal.getTime();
    }
    
    /**
     * Format time from int to string.
     * @param seconds time in seconds.
     * @return time in format HH:mm:ss.
     */
    public static String formatTime(int seconds)
    {
        int hour = 0;
        int minute = 0;
        int second = 0;
        int left = seconds;
        
        if (left / SECONDS_IN_HOUR > 0)
        {
            hour = left / SECONDS_IN_HOUR;
            left = left % SECONDS_IN_HOUR;
        }
        
        if (left / SECONDS_IN_MINUTE > 0)
        {
            minute = left / SECONDS_IN_MINUTE;
            left = left % SECONDS_IN_MINUTE;
        }
        
        second = left;
        
        return TextUtil.lpad(hour + "", 2, '0') + ":" +
            TextUtil.lpad(minute + "", 2, '0') + ":" +
            TextUtil.lpad(second + "", 2, '0');
    }
    
    /**
     * Formats the number to time
     * @param number time in number format
     * @param isSeconds true: number in seconds | false: number in milliseconds
     */
    public static String formatTime(long number, boolean isSeconds)
	{
    	return isSeconds ? formatTime(number * 1000) : 
    		formatTime(number);
	}
    
    private static String formatTime(long number)
    {
    	long milsec = number % 1000;
		long sec = number / 1000;
		
		long hour = 0;
		long minute = 0;
		long seconds = 0;
		long left = sec;
		
		if (sec / SECONDS_IN_HOUR > 0)
		{
			hour = sec / SECONDS_IN_HOUR;
			left = sec % SECONDS_IN_HOUR;
		}
		
		if (left / SECONDS_IN_MINUTE > 0)
		{
			minute = left / SECONDS_IN_MINUTE;
			left = left % SECONDS_IN_MINUTE;
		}
		
		seconds = left;
		
		return TextUtil.lpad(hour + "", 2, '0') + ":" + 
			TextUtil.lpad(minute + "", 2, '0') + ":" + 
			TextUtil.lpad(seconds + "", 2, '0') + "." + 
			TextUtil.lpad(milsec + "", 3, '0');
    }
    
    /**
     * Formats the number to time with the given pattern 
     * for example, return type like ${hh}${mm}${ss}${sss} or ${hh}:${mm}:${ss}:${sss}   
     * 
     * @param number
     * @param isSeconds
     * @param pattern
     * @return formated time
     */
    public static String formatTime(long number, boolean isSeconds, String pattern)
    {
        if(TextUtil.isEmpty(pattern))
            return isSeconds ? formatTime(number * 1000) : 
                formatTime(number);
            
        return isSeconds ? formatTime(number * 1000,pattern) : 
            formatTime(number,pattern);
    }
    
    private static String formatTime(long number, String pattern)
    {
        long milsec = number % 1000;
        long sec = number / 1000;
        
        long hour = 0;
        long minute = 0;
        long seconds = 0;
        long left = sec;
        
        if (sec / SECONDS_IN_HOUR > 0)
        {
            hour = sec / SECONDS_IN_HOUR;
            left = sec % SECONDS_IN_HOUR;
        }
        
        if (left / SECONDS_IN_MINUTE > 0)
        {
            minute = left / SECONDS_IN_MINUTE;
            left = left % SECONDS_IN_MINUTE;
        }
        
        seconds = left;
        
        Map param = new HashMap();
        param.put(HOUR_PART, TextUtil.lpad(hour + "", 2, '0'));
        param.put(MINUTE_PART, TextUtil.lpad(minute + "", 2, '0'));
        param.put(SECOND_PART, TextUtil.lpad(seconds + "", 2, '0'));
        param.put(MILLSECOND_PART, TextUtil.lpad(milsec + "", 3, '0'));
        
        return TextUtil.expand(pattern, param);
    }
    
    /**
     * Gets the millis from time.
     * @param time time format as integer or 'HH:MM:SS' or 'HH:MM', adding .SSS for milliseconds, e.g. 10.100, 00:10.100, 00:10:10.100
     * @return the milliseconds
     */
    public static long time2Millis(String time)
    {
        int millisIdx = time.indexOf('.');
        long millis = 0;
        if (millisIdx != -1)
        {
            String str = TextUtil.rpad(time.substring(millisIdx + 1), 3, '0');
            if (str.length() > 3)
                return -5;
            
            millis = Long.parseLong(str);
        }
        
        int secs = time2Seonds(millisIdx != -1 ? time.substring(0, millisIdx) : time);
        if (secs < 0)
            return secs;
        
        return secs * 1000 + millis;
    }
    
    /**
     * Get total seconds for input time string
     * @param time format as 'HH:MM:SS' or 'HH:MM'
     * @return
     */
    public static int time2Seonds(String time)
    {
        if (TextUtil.isEmpty(time))
            return -1;
        
        if (NumberUtil.parseInteger(time) != null)
            return NumberUtil.parseInteger(time).intValue();
        
        String[] timeArr = TextUtil.split(time, ":");
        if (timeArr.length >= 3)
        {
            Integer hours = NumberUtil.parseInteger(timeArr[0].trim());
            if (hours == null || hours.intValue() < 0)
                return -2;
            
            Integer minutes = NumberUtil.parseInteger(timeArr[1].trim());
            if (isInvalidMinSec(minutes))
                return -3;
            
            Integer seconds = NumberUtil.parseInteger(timeArr[2].trim());
            if (isInvalidMinSec(seconds))
                return -4;
            
            return hours.intValue() * SECONDS_IN_HOUR 
                        + minutes.intValue() * SECONDS_IN_MINUTE 
                        + seconds.intValue();
        }
        else if (timeArr.length == 2)
        {
            Integer hours = NumberUtil.parseInteger(timeArr[0].trim());
            if (hours == null || hours.intValue() < 0)
                return -2;
            
            Integer minutes = NumberUtil.parseInteger(timeArr[1].trim());
            if (isInvalidMinSec(minutes))
                return -3;
            
            return hours.intValue() * SECONDS_IN_HOUR 
                        + minutes.intValue() * SECONDS_IN_MINUTE; 
        }
        
        return -1;
    }

    private static boolean isInvalidMinSec(Integer minutes)
    {
        return minutes == null || minutes.intValue() < 0 || minutes.intValue() >= 60;
    }
    
    /**
     * return the epoch time seconds of a date
     * this seconds is the time from 1970-01-01 
     * @param date date
     * @return the seconds in epoch time
     */
    public static long getEpochTimeInSeconds(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis()/1000;
    }
    
    /**
     * return the date represents by the dateStr
     * only support MM/dd/yyyy MM-dd-yyyy yyyy-MM-dd yyyy/MM/dd
     * @param dateStr -- the date string get from the date calendar in the jsp page
     * @return
     */
    public static Date parseDateCalDate(String dateStr)
    {
        return  parseDateCalDate(dateStr, null);
    }
    
    /**
     * return the date represents by the dateStr
     * only support MM/dd/yyyy MM-dd-yyyy yyyy-MM-dd yyyy/MM/dd
     * @param dateStr -- the date string get from the date calendar in the jsp page
     * @param timezone -- the time zone
     * @return
     */
    public static Date parseDateCalDate(String dateStr, TimeZone timezone)
    {
        if (TextUtil.isEmpty(dateStr))
            return null;
        
        dateStr = dateStr.replace('/', '-');
        try
        {
            int pos = dateStr.indexOf('-');
            if (pos == -1)
                return null;
            
            String firstSeg = dateStr.substring(0, pos);
            Date retDate = null;
            if (firstSeg.length() == 4)
            {
                DateFormat sampleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                if (timezone != null)
                    sampleDateFormat.setTimeZone(timezone);
                retDate = sampleDateFormat.parse(dateStr);
            }
            else if (firstSeg.length() <= 2)
            {
                DateFormat sampleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                if (timezone != null)
                    sampleDateFormat.setTimeZone(timezone);
                retDate = sampleDateFormat.parse(dateStr);
            }
            
            return retDate;
        }
        catch(Exception e)
        {
            return null;
        } 
    }
    
    public static Date dateAddDays(Date oriDate,int addDays)
    {
        return dateAdd(oriDate, Calendar.DATE, addDays);
    }
    
    public static Date dateAddHours(Date oriDate,int addHours)
    {
        return dateAdd(oriDate, Calendar.HOUR, addHours);
    }
    
    public static Date dateAddMinutes(Date oriDate,int addMins)
    {
        return dateAdd(oriDate, Calendar.MINUTE, addMins);
    }
    
    public static Date dateAddSeconds(Date oriDate,int addSeconds)
    {
        return dateAdd(oriDate, Calendar.SECOND, addSeconds);
    }
    
    public static Date dateAddMillseconds(Date oriDate,int addMillseconds)
    {
        return dateAdd(oriDate, Calendar.MILLISECOND, addMillseconds);
    }
    
    /**
     * Gets the date thru.
     * @param dateFrom the date from
     * @param field the calendar field
     * @param amount the changing amount, negative acceptable
     * @return the date thru
     */
    public static Date dateAdd(Date dateFrom, int field, int amount)
    {
        if (amount == 0)
            return dateFrom;

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis((new Date()).getTime());
        cal.setTime(dateFrom);
        cal.add(field, amount);
        return cal.getTime();
    }
    
    public static Date getLatestDate(Date date)
    {
        if(date == null) return null;
        Calendar ori = Calendar.getInstance();
        ori.setTime(date);
        ori.set(Calendar.MINUTE,59);
        ori.set(Calendar.SECOND,59);
        ori.set(Calendar.MILLISECOND,999);
        ori.set(Calendar.HOUR_OF_DAY, 23);

        return new Date(ori.getTimeInMillis());
    }
    
    public static boolean isDate(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) date);
        return (cal.get(Calendar.MILLISECOND) == 0
                && cal.get(Calendar.SECOND) == 0
                && cal.get(Calendar.MINUTE) == 0
                && cal.get(Calendar.HOUR_OF_DAY) == 0);
    }
    
    /**
     * Format a date string from one format to another format.
     * @param date the date string.
     * @param inputformat the original format of the string.
     * @param outputFormat the output format of the string.
     * @return the format result.
     */
    public static String format(String date, String inputformate,
            String outputFormat)
    {
        return format(date, inputformate, outputFormat, null, null);
    }
    
    /**
     * Format a date string from one format to another format by time zone
     * @param date the date string.
     * @param inputformat the original format of the string.
     * @param outputFormat the output format of the string.
     * @param inputTimeZone the input original time zone
     * @param inputTimeZone the input original time zone
     * @return the format result.
     */
    public static String format(String date, String inputformate,
            String outputFormat, TimeZone inputTimeZone, TimeZone outputTimeZone)
    {
        if (date == null || date.length() == 0)
        {
            return null;
        }
        try
        {
            DateFormat format1 = new SimpleDateFormat(inputformate);
            if (inputTimeZone != null)
                format1.setTimeZone(inputTimeZone);
            Date d = format1.parse(date);

            DateFormat format2 = new SimpleDateFormat(outputFormat);
            if (outputTimeZone != null)
                format2.setTimeZone(outputTimeZone);
            return format2.format(d);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Format a date string.
     * @param date the date string.
     * @param outputFormat the output format of the string.
     * @return the format result.
     */
    public static String format(Date date, String outputFormat)
    {
        return format(date, outputFormat, null);
    }
    
    /**
     * Format a date string by timezone.
     * @param date the date string.
     * @param outputFormat the output format of the string.
     * @param zone the time zone.
     * @return the format result.
     */
    public static String format(Date date, String outputFormat, TimeZone zone)
    {
        if (date == null)
            return "";
        
        DateFormat format = new SimpleDateFormat(outputFormat);
        if (zone != null)
            format.setTimeZone(zone);
        return format.format(date);
    }

    /**
     * input second, return hh:mi:ss format
     * @param sec
     * @return
     */
    public static String formatMinuteWithHour(int sec)
    {
        int hour = sec/60/60;
        int minute = (sec- hour*60*60)/60;
        int second = sec - hour*60*60 - minute *60;
        String result = "";
        if (hour> 0)
        {
            result = hour >= 10? String.valueOf(hour)+ ":": "0"+hour + ":";
        }
        result+= (minute >= 10 ? String.valueOf(minute):"0"+minute);
        result+=":" + (second >= 10 ? String.valueOf(second):"0"+second);
        return result;
    }

    /**
     * 
     * @param sec
     * @return
     */
    public static String formatMinute(int sec)
    {
        String result = "";
        int minute = sec/60;
        int second = sec - minute * 60;
        result+= (minute >= 10 ? String.valueOf(minute):"0"+minute);
        result+=":" + (second >= 10 ? String.valueOf(second):"0"+second);
        return result;
    }
    
    public static int getMonthMaxDays(int year, int month)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        return cal.getActualMaximum(Calendar.DATE);
    }

    /**
     * input date, return HH:MM
     * @param date
     * @return
     */
    public static String getTimeOfDate(Date date)
    {
        return getTimeOfDate(date, null);
    }
    
    /**
     * input date, return HH:MM
     * @param date
     * @param time the time zone
     * @return
     */
    public static String getTimeOfDate(Date date, TimeZone timezone)
    {
        String result = "";
        if (date != null)
        {
            result = formatDate(date, YYYY_MM_DD_HHMMSS, timezone);
            result = result.substring(11, 16);
        }
        return result;
    }

    public static String get12HourTimeOfDate(Date date, boolean showSec)
    {
        String result = "";
        if (date != null)
        {
            if (showSec)
            {
                result = formatDate(date, MM_DD_YYYY_hhMMss);
                result = result.substring(11);
            }
            else
            {
                result = formatDate(date, MM_DD_YYYY_hhMM);
                result = result.substring(11, 16);
            }
        }
        return result;
    }

    /**
     * Get the date time AM/PM string
     * @param date the Date
     * @return am/pm string
     */
    public static String getAMPM(Date date)
    {
        return getAMPM(date, null);
    }
    
    /**
     * Get the date time AM/PM string by time zone
     * @param date the Date
     * @param timeZone the time zone
     * @return am/pm string
     */
    public static String getAMPM(Date date, TimeZone timeZone)
    {
        Calendar cal = Calendar.getInstance();
        if (timeZone != null)
            cal.setTimeZone(timeZone);
        cal.setTime(date);
        String result = "am";
        int ampm = cal.get(Calendar.AM_PM);
        if (ampm == 1)
        {
            result = "pm";
        }
        return result;
    }

    /**
     * @param dateStr: MM/dd/yyyy
     * @param timeStr: HH:MM
     * @return
     */
    public static Date getDate(String dateStr, String timeStr)
    {
        return getDate(dateStr, timeStr, null);
    }
    
    /**
     * @param dateStr: MM/dd/yyyy
     * @param timeStr: HH:MM
     * @param timezone the time zone
     * @return
     */
    public static Date getDate(String dateStr, String timeStr, TimeZone timezone)
    {
        String fullDateStr = dateStr.trim() + " " + timeStr.trim();
        return parseDate(fullDateStr, MM_DD_YYYY_HHMM, timezone);
    }

    /**
     * Get the calendar field value.
     * @param date the date
     * @param field the calender field
     * @return the value of the field
     */
    public static int getFieldValue(Date date, int field)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis((new Date()).getTime());
        cal.setTime(date);
        return cal.get(field);
    }

    /**
     * Returns whether date is between dateFrom and dateThru, allowing nulls.
     * @param dateFrom the date from inclusive, null allowed
     * @param dateThru the date thru exclusice, null allowed
     * @return whether date is between dateFrom and dateThru, returning true if date is null
     */
    public static boolean isBetween(Date date, Date dateFrom, Date dateThru)
    {
        if (date == null)
            return true;
        
        return (dateThru == null || date.getTime() < dateThru.getTime())
            && (dateFrom == null || date.getTime() >= dateFrom.getTime());
    }

    /**
     * Compares 2 dates.
     * @param date1 date 1, null means infinity
     * @param date2 date 2, null means infinity
     * @return 0 if equals, positive if date 1 is bigger 
     */
    public static int compare(Date date1, Date date2)
    {
        if (date1 == null && date2 == null)
            return 0;
        
        if (date1 == null)
            return 1;
        if (date2 == null)
            return -1;
        long diff = date1.getTime() - date2.getTime();
        return (diff == 0) ? 0 : (diff > 0 ? 1 : -1);
    }

    /**
     * Gets all time zones.
     * @return the array of time zone id/name pair 
     */
    public static String[][] getTimeZones(Locale locale)
    {
        String[] ids = TimeZone.getAvailableIDs();
        List zones = new ArrayList();
        //Set names = new HashSet();
        for (int i = 0; i < ids.length; i++)
        {
            TimeZone tz = TimeZone.getTimeZone(ids[i]);
            int minOffset = tz.getRawOffset() / (1000 * 60);
            String name = "(GMT" + timeToString(minOffset / 60, true)
                    + ":" + timeToString(minOffset % 60, false) + ") "
                    + tz.getDisplayName(locale == null ? Locale.getDefault() : locale)
                    + " - " + tz.getID();
            zones.add(new String[] {ids[i], name, String.valueOf(minOffset)});
/*            if (names.add(name))
                zones.add(new String[] {ids[i], name});
            else if (log.isDebugEnabled())
                log.debug("Ignore duplicate name: " + name + ", id: " + ids[i]);*/
        }
        Collections.sort(zones, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return NumberUtil.compare(Integer.valueOf(((String[]) o1)[2]), Integer.valueOf(((String[]) o2)[2]));
            }
        });
        return (String[][]) zones.toArray(new String[zones.size()][2]);
    }

    private static String timeToString(int num, boolean hasSign)
    {
        String sign = null;
        if (hasSign)
        {
            if (num >= 0)
            {
                sign = "+";
            }
            else
            {
                sign = "-";
                num = Math.abs(num);
            }
        }
        else
            num = Math.abs(num);
        
        String numStr = TextUtil.lpad(String.valueOf(num), 2, '0');
        return sign == null ? numStr : sign + numStr;
    }
    
    public static int getYear(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return year;
    }
    
    public static int getHour(Date date, TimeZone timezone)
    {
    	if(timezone == null)
    		timezone = TimeZone.getDefault();
    	Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTimeZone(timezone);
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour;
    }
    
    public static int getMinute(Date date)
    {
    	Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        int minute = cal.get(Calendar.MINUTE);
        return minute;
    }
    
    public static int getSecond(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        int second = cal.get(Calendar.SECOND);
        return second;
    }
    
    /**
     * Get the DST start time 
     * @param date the date(format: MM/dd/yyyy)
     * @param timeZone the time zone.
     * @return the DST start time(02:00:00), null means date is not DST.
     */
    public static String getDSTStartTime(String date, TimeZone timeZone)
    {
        String startTime = null;
        Date date2 = parseDate(date, MM_DD_YYYY, timeZone);
        Date nextDate = dateAdd(date2, Calendar.DATE, 1);
    	if(!timeZone.inDaylightTime(date2) && timeZone.inDaylightTime(nextDate))
    	{
    		for (int i = 0; i < 23; i++)
        	{
    			Date hourDate =  dateAdd(date2, Calendar.HOUR_OF_DAY, i);
        		Date nextHour = dateAdd(date2, Calendar.HOUR_OF_DAY, i + 1);
        		if(!timeZone.inDaylightTime(hourDate) && timeZone.inDaylightTime(nextHour))
    			{
        			startTime = DateTimeUtil.getHour(hourDate, timeZone) + 1 + ":00:00";
    			}
        	}
    	}
        return startTime;
    }
    
    /**
     * Get the DST end time 
     * @param date the date(format: MM/dd/yyyy)
     * @param timeZone the time zone.
     * @return the DST end time(02:00:00), null means date is not DST.
     */
    public static String getDSTEndTime(String date, TimeZone timeZone)
    {
    	String startTime = null;
        Date date2 = parseDate(date, MM_DD_YYYY, timeZone);
        Date nextDate = dateAdd(date2, Calendar.DATE, 1);
    	if(timeZone.inDaylightTime(date2) && !timeZone.inDaylightTime(nextDate))
    	{
    		for (int i = 0; i < 23; i++)
        	{
    			Date hourDate =  dateAdd(date2, Calendar.HOUR_OF_DAY, i);
        		Date nextHour = dateAdd(date2, Calendar.HOUR_OF_DAY, i + 1);
        		if(timeZone.inDaylightTime(hourDate) && !timeZone.inDaylightTime(nextHour))
    			{
        			nextHour = dateAdd(nextHour, Calendar.HOUR_OF_DAY, 1);
        			startTime = DateTimeUtil.getHour(nextHour, timeZone) + ":00:00";
    			}
        	}
    	}
        return startTime;
    }
    
    public static int getMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        return month + 1;
    }
    
    public static Date getFirstDateOfMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        
        return cal.getTime();
    }
    
    public static Date getLastDateOfMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        cal.set(Calendar.MILLISECOND,999);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        
        return cal.getTime();
    }
    
    public static Date getFirstTimeOfDate(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }
    
    /**
     * floor the time minute to be beginning of the 10 minutes.
     * E.g., 08:03 will return 08:00, 08:59 will return 08:50.
     */
    public static Date floorBy10Min(Date date)
    {
        if (date == null)
            return null;
        
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(date);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(calFrom.get(Calendar.YEAR), calFrom.get(Calendar.MONTH),
                calFrom.get(Calendar.DATE));
        cal.set(Calendar.HOUR_OF_DAY, calFrom.get(Calendar.HOUR_OF_DAY));
        int min = calFrom.get(Calendar.MINUTE);
        min = min / 10 * 10;
        cal.set(Calendar.MINUTE, min);
        return cal.getTime();
    }
    
    public static String dateAppendFmt(Date date)
    {
        return DateTimeUtil.format(date, DateTimeUtil.YYYYMMDD_F, TimeZone.getDefault());
    }
}
