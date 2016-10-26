package fr.ourten.brokkgui.validation;

import org.apache.commons.lang3.StringUtils;

public class AsciiPrintableTextValidator extends BaseTextValidator
{
    public AsciiPrintableTextValidator()
    {
        this.setMessage("Only printable characters are supported !");
    }

    @Override
    public boolean eval(final String data)
    {
        return StringUtils.isAsciiPrintable(data);
    }
}