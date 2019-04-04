package net.voxelindustry.brokkgui.data;

public enum RectCorner
{
    TOP_LEFT("top-left"),
    TOP_RIGHT("top-right"),
    BOTTOM_LEFT("bottom-left"),
    BOTTOM_RIGHT("bottom-right");

    private String cssString;

    RectCorner(String cssString)
    {
        this.cssString = cssString;
    }

    public String getCssString()
    {
        return cssString;
    }
}
