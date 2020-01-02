package net.voxelindustry.brokkgui.layout;

public enum SizeUnit
{
    PIXEL("px"),
    PARENT_RATIO("%"),
    PARENT_WIDTH_RATIO("pw"),
    PARENT_HEIGHT_RATIO("ph"),
    PARENT_MIN_RATIO("pmin"),
    PARENT_MAX_RATIO("pmax"),
    FONT_SIZE("em"),
    FONT_HEIGHT("ex"),
    ZERO_CHAR_WIDTH("ch");

    private String unit;

    SizeUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }
}
