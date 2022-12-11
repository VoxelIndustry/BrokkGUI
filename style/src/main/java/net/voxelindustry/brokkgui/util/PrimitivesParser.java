package net.voxelindustry.brokkgui.util;

public class PrimitivesParser
{
    public static int intLength(String value)
    {
        int count = 0;

        for (int index = 0; index < value.length(); index++)
        {
            // Early return to prevent parsing a floating number
            if (value.charAt(index) == '.')
                return 0;

            if (!Character.isDigit(value.charAt(index)) && ((index != 0 || value.charAt(index) != '+') && value.charAt(index) != '-'))
                break;
            count++;
        }
        return count;
    }

    public static int floatLength(String str)
    {
        int count = 0;
        boolean decimalPart = false;

        for (int index = 0; index < str.length(); index++)
        {
            if (str.charAt(index) == '.' && !decimalPart)
                decimalPart = true;
            else if (!Character.isDigit(str.charAt(index)) && ((index != 0 || str.charAt(index) != '+') && str.charAt(index) != '-'))
                break;
            count++;
        }
        return count;
    }

    public static int boolLength(String str)
    {
        if (str.equalsIgnoreCase("true"))
            return 4;
        else
            return 5;
    }
}
