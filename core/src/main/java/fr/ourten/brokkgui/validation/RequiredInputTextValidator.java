package fr.ourten.brokkgui.validation;

import org.apache.commons.lang3.StringUtils;

public class RequiredInputTextValidator extends BaseTextValidator
{
    public RequiredInputTextValidator()
    {
        this.setMessage("Cannot be blank. Input is required !");
    }

    @Override
    public boolean eval(final String data)
    {
        return StringUtils.isEmpty(data);
    }
}