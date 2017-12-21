/*
 * Copyright (c) 2017 Du Tengfei. All Rights Reserved.
 */
package com.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * NumberUtil.
 * @author <A HREF="mailto:dtfgongzuo@163.com">Du Tengfei</A>
 * @version 1.0, $Revision: 0$, $Date: Nov 19, 2017$
 * @since 1.0
 */
public class NumberUtil
{
	
	 private final static String[] hexDigits = {
		       "0", "1", "2", "3", "4", "5", "6", "7",
		       "8", "9", "a", "b", "c", "d", "e", "f"}; 	
     
     public static BigDecimal ZERO = new BigDecimal("0.00");
     
    /**
     * Parses an  integer.
     * @param num the number string
     * @return the integer, null if not an integer
     */
    public static Integer parseInteger(String num)
    {
        if (TextUtil.isEmpty(num))
            return null;

        try
        {
            return new Integer(num);
        }
        catch (Exception ex)
        {
            return null;
        }    	
    }

    /**
     * Parse strings to integers, filtering non-integer strings.
     * @param nums the numbers of string
     * @return the integers
     */
    public static int[] parseIntegers(String[] nums)
    {
        List list = new ArrayList();
        for (int i = 0; i < nums.length; i++)
        {
            Integer in = parseInteger(nums[i]);
            if (in != null)
                list.add(in);
        }
        
        int[] ret = new int[list.size()];
        int j = 0;
        for (Iterator itr = list.iterator(); itr.hasNext();)
        {
            Integer in = (Integer) itr.next();
            ret[j ++] = in.intValue();
        }
        
        return ret;
    }

    /**
     * Parse strings to integers, filtering non-integer strings.
     * @param nums the numbers of string
     * @return the integers
     */
    public static Integer[] parseIntegers2(String[] nums)
    {
        List list = new ArrayList();
        for (int i = 0; i < nums.length; i++)
        {
            Integer in = parseInteger(nums[i]);
            if (in != null)
                list.add(in);
        }
        
        return (Integer[]) list.toArray(new Integer[0]);
    }
    
    /**
     * Parses a string of numbers separated by some delimiter.
     * @param numstr the numbers
     * @param delimiter the delimitor
     * @return the ints
     */
    public static int[] parseIntegers(String numstr, String delimitor)
    {
        if (delimitor == null)
            delimitor = ",";
        String[] nums = TextUtil.split(numstr, delimitor);
        return parseIntegers(nums);
    }
    
    /**
     * Parses a string of numbers separated by some delimiter.
     * @param numstr the numbers
     * @param delimiter the delimitor
     * @return the ints
     */
    public static Integer[] parseIntegers2(String numstr, String delimitor)
    {
        if (delimitor == null)
            delimitor = ",";
        String[] nums = TextUtil.split(numstr, delimitor);
        return parseIntegers2(nums);
    }
    
    /**
     * Unboxes the integer array.
     * @param integers the integers
     * @return the ints
     */
    public static int[] unbox(Integer[] integers)
    {
        if (integers == null)
            return null;
        int[] ret = new int[integers.length];
        for (int i = 0; i < integers.length; i++)
        {
            ret[i] = integers[i].intValue();
        }
        return ret;
    }
    
    /**
     * Unboxes the integer collection.
     * @param col the collection
     * @return the ints
     */
    public static int[] unbox(Collection col)
    {
        if (col == null)
            return null;
        int[] ret = new int[col.size()];
        int j = 0;
        for (Iterator itr = col.iterator(); itr.hasNext();)
        {
            Integer in = (Integer) itr.next();
            ret[j++] = in.intValue();
        }
        return ret;
    }
    
    /**
     * Parses an double.
     * @param num the number string
     * @return the double, null if not an double
     */
    public static Double parseDouble(String num)
    {
        if (TextUtil.isEmpty(num))
            return null;

        try
        {
            return new Double(num);
        }
        catch (Exception ex)
        {
            return null;
        }       
    }
    
    /**
     * Formats the amount.
     * @param amount the amount
     * @return the string
     */
    public static String formatAmount(BigDecimal amount)
    {
        NumberFormat fmt = NumberFormat.getInstance();
        fmt.setMaximumFractionDigits(2);
        fmt.setMinimumFractionDigits(2);
        fmt.setParseIntegerOnly(false);
        return fmt.format(amount);
    }   
    
    /**
     * Formats the amount with currency code.
     * @param amount the amount
     * @param currencyCode the currency code
     * @return the string
     */
    public static String formatAmount(BigDecimal amount, String currencyCode, Locale locale)
    {
        if (locale == null)
            locale = Locale.getDefault();
        
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        if (currencyCode != null)
        {
            fmt.setCurrency(Currency.getInstance(currencyCode));
        }
        return fmt.format(amount);
    }
    
    /**
     * Is a number positive?
     * @param num the number
     * @return true if positive
     */
    public static boolean isPositive(Number num)
    {
        if (num == null)
            return false;
        
        if (num instanceof BigDecimal)
            return ZERO.compareTo((BigDecimal) num) < 0;
            
        return num.doubleValue() > 0;
    }

    /**
     * Is a number negative?
     * @param num the number
     * @return true if negative
     */
    public static boolean isNegative(Number num)
    {
        if (num == null)
            return false;
        
        if (num instanceof BigDecimal)
            return ZERO.compareTo((BigDecimal) num) > 0;
            
        return num.doubleValue() < 0;
    }
    
    /**
     * Is a number zero?
     * @param num the number
     * @return true if zero
     */
    public static boolean isZero(Number num)
    {
        if (num == null)
            return true;
        
        if (num instanceof BigDecimal)
            return ZERO.compareTo((BigDecimal) num) == 0;
            
        return num.doubleValue() == 0;
    }
    
    /**
     * Gets the percent of a number.
     * @param num the number
     * @param percent the percent
     * @return the result
     */
    public static BigDecimal percent(BigDecimal num, Number percent, int decimalSupport)
    {
        BigDecimal percent_;
        if (percent instanceof BigDecimal)
            percent_ = (BigDecimal) percent;
        else
            percent_ = new BigDecimal(percent.doubleValue());

        return num.multiply(percent_).divide(new BigDecimal(100d), 
        		decimalSupport,
                BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Gets the percent of a number.
     * @param num the number
     * @param percent the percent
     * @return the result
     */
    public static BigDecimal percent(BigDecimal num, double percent, int decimalSupport)
    {
        return percent(num, new Double(percent), decimalSupport);
    }

    /**
     * Gets the multiplied number.
     * @param num the number
     * @param quantity the quantity
     * @return the result
     */
    public static BigDecimal multiply(BigDecimal num, Number quantity, int decimalSupport)
    {
        BigDecimal quantity_;
        if (quantity instanceof BigDecimal)
            quantity_ = (BigDecimal) quantity;
        else
            quantity_ = new BigDecimal(quantity.doubleValue());
        return num.multiply(quantity_).setScale(decimalSupport,
                BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Gets the multiplied number.
     * @param num the number
     * @param quantity the quantity
     * @return the result
     */
    public static BigDecimal multiply(BigDecimal num, double quantity, int decimalSupport)
    {
        return num.multiply(new BigDecimal(quantity)).setScale(decimalSupport,
                BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Are numbers equal in value?
     * @param num1 the number 1
     * @param num2 the number 2
     * @return true if equals in value
     */
    public static boolean equalsValue(Number num1, Number num2)
    {
        return (num1 == null) ? (num2 == null) : 
            ((num2 == null) ? false : 
                (num1.doubleValue() == num2.doubleValue()));
    }
    
    /**
     * Convert a byte to HEX string format For example, input is 16, then the
     * output will be 10
     * @param b -- input byte
     * @return the HEX string format of input byte
     */
    public static String byteToHexString(byte b)
    {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static BigDecimal newBigDecimal(int i)
    {
        return new BigDecimal((double) i);
    }

    public static BigDecimal newBigDecimal(long l)
    {
        return new BigDecimal((double) l);
    }

    public static BigDecimal getSmaller(BigDecimal num1, BigDecimal num2)
    {
        return num1.compareTo(num2) > 0 ? num2 : num1;
    }

    public static boolean isSame(Integer int1, Integer int2)
    {
        if (int1 == null && int2 == null)
            return true;
        if (int1 == null || int2 == null)
            return false;
        return int1.equals(int2);
    }
     
    public static BigDecimal getDiscount(BigDecimal originalAmount,
            Double discountPrcent, BigDecimal discountAmount,
            boolean canExceedOriginal, int decimalSupport)
    {
        if (NumberUtil.isPositive(discountPrcent))
        {
            discountAmount = NumberUtil.percent(originalAmount, new BigDecimal(
                    discountPrcent.doubleValue()), decimalSupport);
        }
        else if (!canExceedOriginal
                && discountAmount.compareTo(originalAmount) > 0)
        {
            discountAmount = originalAmount;
        }
        return discountAmount;
    }
    
    /**
     * =ROUNDUP(
     *   IF(
     *     MOD(
     *       (C2*C3),
     *       0.01*10^C5
     *     )
     *     >
     *     0.005*10^C5
     *     ,
     *     ROUNDUP(
     *       (C2*C3),
     *       (C5-2*(C5-1))
     *     )
     *     -
     *     C6
     *     ,
     *     ROUNDUP(
     *       (C2*C3),
     *       (C5-2*(C5-1))
     *     )
     *     -
     *     IF(
     *       C7="yes",
     *       (0.005*10^C5)
     *       ,
     *       0
     *     ) 
     *     - 
     *     C6
     *   )
     *   ,
     *   C4
     * )
     * @param primaryCurrencyAmount C2
     * @param exRate C3
     * @param decimalSupport C4
     * @param significantPricePosition C5
     * @param priceEndingOffset C6
     * @param halfPricePositionRounding C7
     */
    public static BigDecimal exchange(BigDecimal primaryCurrencyAmount, BigDecimal exRate, int decimalSupport, int significantPricePosition, BigDecimal priceEndingOffset, String halfPricePositionRounding)
    {
        BigDecimal rtn = null;
        BigDecimal c2c3 = primaryCurrencyAmount.multiply(exRate);
        int scale = significantPricePosition-2*(significantPricePosition-1);
        BigDecimal c2c3RoundUp = setScale(c2c3, scale, BigDecimal.ROUND_UP);
        BigDecimal c2c3RoundHalfDown = setScale(c2c3, scale, BigDecimal.ROUND_HALF_DOWN);
        if (c2c3RoundUp.compareTo(c2c3RoundHalfDown) == 0)
        {
            rtn = c2c3RoundUp.subtract(priceEndingOffset);
        }
        else
        {
            if ("yes".equalsIgnoreCase(halfPricePositionRounding))
            {
                BigDecimal halfPriceEndingOffset = setScale(new BigDecimal(Math.pow(10, significantPricePosition-2) / 2), scale+1, BigDecimal.ROUND_HALF_UP);
                rtn = c2c3RoundUp.subtract(halfPriceEndingOffset).subtract(priceEndingOffset);
            }
            else
            {
                rtn = c2c3RoundUp.subtract(priceEndingOffset);
            }
        }
        
        return setScale(rtn, decimalSupport, BigDecimal.ROUND_UP);
    }
    
    private static BigDecimal setScale(BigDecimal amount, int scale, int roundMode)
    {
        if (scale >= 0) return amount.setScale(scale, roundMode);
        BigDecimal multiplier = new BigDecimal(Math.pow(10d, -scale)).setScale(0);
        BigDecimal rtn = amount.divide(multiplier, 0, roundMode).multiply(multiplier);
        return rtn;
    }
    
    /**
     * Compares 2 Integer.
     * @param int1 integer 1, null means infinity
     * @param int2 integer 2, null means infinity
     * @return 0 if equals, positive if integer 1 is bigger 
     */
    public static int compare(Integer int1, Integer int2)
    {
        if (int1 == null && int2 == null)
            return 0;
        if (int1 == null)
            return 1;
        if (int2 == null)
            return -1;
        return int1.intValue() > int2.intValue()?1:(int1.intValue() == int2.intValue()?0:-1);
    }
    
    /**
     * Parses an  Short.
     * @param num the number string
     * @return the Short, null if not an Short
     */
    public static Short parseShort(String num)
    {
        if (TextUtil.isEmpty(num))
            return null;

        try
        {
            return Short.valueOf(num);
        }
        catch (Exception ex)
        {
            return null;
        }       
    }
    
    /**
     * Parse size string like 1K, 1M, 1G to long value bytes
     * such as 1024, 2*1024, 1024*1024*1024
     * @param sizeString
     * @param defaultSize
     * @return
     */
    public static long parseSizeToBytes(String sizeString, long defaultSize)
    {
        int multiplier = 1024;
        String[] units = {"k", "m", "g"};
        for (int i = 0; i < units.length; i++)
        {
            String unit = units[i];
            if (sizeString.toLowerCase().endsWith(unit))
            {
                multiplier = Double.valueOf(Math.pow(multiplier, i+1)).intValue();
                sizeString = sizeString.substring(0, sizeString.length()-1);
                break;
            }
        }
        
        long size = 0;
        try
        {
            size = Long.parseLong(sizeString); 
        }
        catch (Exception e)
        {
            size = defaultSize;
            multiplier = 1;
        }

        return (size * multiplier);
    }
    
    /**
     * Convert byte value in long to size string like 100B,100M,100G, 
     * support up to TB level
     * 
     * @param bytes in double
     * @return
     */
    public static String parseBytesToSizeString(double bytes)
    {
        if (bytes <= 0)
            return "0 B";
        
        String[] units = {"B", "KB", "MB", "GB", "TB", "PB"};
        int unit = (int)(Math.log10(bytes)/Math.log10(1024));
        return unit > (units.length-1)?"OVERSIZE":new DecimalFormat("#,##0.##").format(bytes
                / Math.pow(1024, unit))+" "+units[unit];
    }
}
