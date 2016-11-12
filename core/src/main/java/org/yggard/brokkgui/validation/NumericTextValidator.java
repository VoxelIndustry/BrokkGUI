package org.yggard.brokkgui.validation;

import org.apache.commons.lang3.StringUtils;

public class NumericTextValidator extends BaseTextValidator
{
    public NumericTextValidator()
    {
        this.setMessage("Only numbers are allowed !");
    }

    @Override
    public boolean eval(final String data)
    {
        return StringUtils.isNumeric(data);
    }
}