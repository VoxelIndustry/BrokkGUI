package net.voxelindustry.brokkgui.util;

public class StringCountUtils
{
    public static int integerAtStart(String str)
    {
        int count = 0;

        for (int index = 0; index < str.length(); index++)
        {
            // Early return to prevent parsing a floating number
            if (str.charAt(index) == '.')
                return 0;
            
            if (!Character.isDigit(str.charAt(index)) && ((index != 0 || str.charAt(index) != '+') && str.charAt(index) != '-'))
                break;
            count++;
        }
        return count;
    }

    public static int floatAtStart(String str)
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

    public static int boolArtStart(String str)
    {
        if (str.equalsIgnoreCase("true"))
            return 4;
        else
            return 5;
    }
}
