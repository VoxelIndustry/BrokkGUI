package fr.ourten.brokkgui.policy;

public enum EOverflowPolicy
{
    /**
     * NONE : Nothing is trimmed, inside elements can go everywhere they want.
     *
     * TRIM : All classical inside elements are trimmed, only special-cased
     * stuff like ToolTips are ignored.
     *
     * TRIM_ALL : Absolutely everything is trimmed.
     */
    NONE, TRIM, TRIM_ALL;
}