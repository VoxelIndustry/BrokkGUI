package net.voxelindustry.brokkgui.validation;

import org.apache.commons.lang3.math.NumberUtils;

public class NumericTextValidator extends BaseTextValidator
{
    public NumericTextValidator()
    {
        this(null);
    }

    public NumericTextValidator(String message)
    {
        setMessage(message == null ? "Only parsable numbers are allowed" : message);
    }

    @Override
    public boolean eval(String data)
    {
        return NumberUtils.isNumber(data);
    }
}