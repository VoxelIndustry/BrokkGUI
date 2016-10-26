package fr.ourten.brokkgui.data;

import java.util.Comparator;

import fr.ourten.brokkgui.component.GuiNode;

public class ZLevelComparator implements Comparator<GuiNode>
{
    @Override
    public int compare(final GuiNode c1, final GuiNode c2)
    {
        return c1.getzLevel() > c2.getzLevel() ? 1 : c1.getzLevel() == c2.getzLevel() ? 0 : -1;
    }
}