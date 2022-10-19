package net.voxelindustry.brokkgui.validation;

import org.apache.commons.lang3.StringUtils;

public class DigitsTextValidator extends BaseTextValidator
{
    public DigitsTextValidator()
    {
        setMessage("Only digits are allowed");
    }

    @Override
    public boolean eval(String data)
    {
        return StringUtils.isNumeric(data);
    }
}