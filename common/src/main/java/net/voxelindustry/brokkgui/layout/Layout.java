package net.voxelindustry.brokkgui.layout;

import fr.ourten.teabeans.value.Observable;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.UpdateComponent;

import java.util.ArrayList;
import java.util.List;

public class Layout extends GuiComponent implements UpdateComponent
{
    private final List<LayoutElement> layoutElements;

    private boolean isDirty;

    public Layout()
    {
        this.layoutElements = new ArrayList<>();
    }

    public void addLayout(LayoutComponent layoutComponent)
    {
        this.layoutElements.add(new LayoutElement(new LayoutBox(layoutComponent.xPos(), layoutComponent.yPos(), layoutComponent.preferredWidth(), layoutComponent.preferredHeight()), layoutComponent));
    }

    void refresh(float desiredWidth, float desiredHeight)
    {

    }

    private void onPropertyChange(Observable obs)
    {
        this.isDirty = true;
    }

    @Override
    public void update()
    {
        if (this.isDirty)
            this.refresh(((LayoutDelegate) element()).desiredWidth(), ((LayoutDelegate) element()).desiredHeight());
    }
}
