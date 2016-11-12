package org.yggard.brokkgui.validation;

public interface IDataValidator<T>
{
    void setMessage(String message);

    String getMessage();

    boolean validate(T data);

    boolean eval(T data);
}