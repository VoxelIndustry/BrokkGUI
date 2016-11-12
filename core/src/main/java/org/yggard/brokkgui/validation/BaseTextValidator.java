package org.yggard.brokkgui.validation;

public abstract class BaseTextValidator implements ITextValidator
{
    private String  message;
    private boolean isErrored;

    @Override
    public boolean validate(final String data)
    {
        if (!this.eval(data))
            this.setErrored(true);
        return this.isErrored;
    }

    public boolean isErrored()
    {
        return this.isErrored;
    }

    public void setErrored(final boolean isErrored)
    {
        this.isErrored = isErrored;
    }

    @Override
    public String toString()
    {
        return "BaseTextValidator [isErrored=" + this.isErrored + "]";
    }

    @Override
    public void setMessage(final String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return this.message;
    }
}