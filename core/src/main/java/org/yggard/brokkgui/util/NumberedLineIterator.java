package org.yggard.brokkgui.util;

import org.apache.commons.io.LineIterator;

import java.io.Reader;

public class NumberedLineIterator extends LineIterator
{
    private int lineNumber;

    public NumberedLineIterator(Reader reader) throws IllegalArgumentException
    {
        super(reader);

        this.lineNumber = -1;
    }

    public String nextLine()
    {
        lineNumber++;
        return super.nextLine();
    }

    public int getLineNumber()
    {
        return lineNumber;
    }
}
