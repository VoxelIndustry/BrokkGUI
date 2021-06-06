package net.voxelindustry.brokkgui.validation;

import org.apache.commons.lang3.StringUtils;

public class AsciiPrintableTextValidator extends BaseTextValidator
{
    public AsciiPrintableTextValidator()
    {
        setMessage("Only printable characters are supported");
    }

    @Override
    public boolean eval(String data)
    {
        return StringUtils.isAsciiPrintable(data);
    }
}