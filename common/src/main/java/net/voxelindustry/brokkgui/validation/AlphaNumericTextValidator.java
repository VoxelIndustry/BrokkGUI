package net.voxelindustry.brokkgui.validation;

import org.apache.commons.lang3.StringUtils;

public class AlphaNumericTextValidator extends BaseTextValidator
{
    public AlphaNumericTextValidator()
    {
        setMessage("Invalid input, only AlphaNumeric text is supported");
    }

    @Override
    public boolean eval(String data)
    {
        return StringUtils.isAlphanumeric(data);
    }
}
