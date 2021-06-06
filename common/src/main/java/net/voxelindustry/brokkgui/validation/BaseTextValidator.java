package net.voxelindustry.brokkgui.validation;

public abstract class BaseTextValidator implements ITextValidator
{
    private String  message;
    private boolean invalid;

    @Override
    public boolean validate(String data)
    {
        if (!eval(data))
            setInvalid(true);
        return invalid;
    }

    public boolean isInvalid()
    {
        return invalid;
    }

    public void setInvalid(boolean isErrored)
    {
        invalid = isErrored;
    }

    @Override
    public String toString()
    {
        return "BaseTextValidator [isErrored=" + invalid + "]";
    }

    @Override
    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}