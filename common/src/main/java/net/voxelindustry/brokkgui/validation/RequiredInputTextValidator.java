package net.voxelindustry.brokkgui.validation;

import org.apache.commons.lang3.StringUtils;

public class RequiredInputTextValidator extends BaseTextValidator
{
    public RequiredInputTextValidator()
    {
        setMessage("Cannot be blank. Input is required");
    }

    @Override
    public boolean eval(String data)
    {
        return StringUtils.isEmpty(data);
    }
}