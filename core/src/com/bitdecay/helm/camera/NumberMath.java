package com.bitdecay.helm.camera;

/**
 * Simple math functions with simple numbers
 *
 * @author Michael P. Wingfield
 * @copyright Nov 4, 2014
 *
 */
public class NumberMath
{
    /**
     * Rounds a float to nearest whole number
     *
     * @param num
     * @return
     */
    public static float round(float num)
    {
        return round(num, 0);
    }


    /**
     * Rounds a float to the number of decimal places
     *
     * @param num
     * @param decimalPlaces
     * @return
     */
    public static float round(float num, int decimalPlaces)
    {
        float digit = 1;
        for (int i = 0; i < decimalPlaces; i++)
        {
            digit *= 10;
        }
        return Math.round(num * digit) / digit;
    }


    /**
     * Gets the Greatest Common Denominator of a group of whole numbers
     *
     * @param numbers
     * @return
     */
    public static int gcd(int... numbers)
    {
        if (numbers == null)
        {
            return 0;
        } else if (numbers.length == 1)
        {
            return numbers[0];
        } else
        {
            int b = numbers[0];
            int a;
            for (int i = 1; i < numbers.length; i++)
            {
                a = numbers[i];
                b = gcdRecurse(a, b);
            }
            return b;
        }
    }


    private static int gcdRecurse(int a, int b)
    {
        return (b == 0 ? a : gcdRecurse(b, a % b));
    }
}