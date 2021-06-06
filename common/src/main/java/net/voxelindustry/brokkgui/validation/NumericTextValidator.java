package net.voxelindustry.brokkgui.validation;

import org.apache.commons.lang3.StringUtils;

public class NumericTextValidator extends BaseTextValidator
{
    public NumericTextValidator()
    {
        setMessage("Only numbers are allowed");
    }

    @Override
    public boolean eval(String data)
    {
        return StringUtils.isNumeric(data);
    }
}