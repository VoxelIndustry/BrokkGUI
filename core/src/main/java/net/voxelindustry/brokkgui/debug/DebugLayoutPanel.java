package net.voxelindustry.brokkgui.debug;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.element.GuiListCell;
import net.voxelindustry.brokkgui.element.GuiListView;
import net.voxelindustry.brokkgui.gui.IGuiWindow;
import net.voxelindustry.brokkgui.panel.GuiAbsolutePane;
import net.voxelindustry.brokkgui.policy.GuiOverflowPolicy;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class DebugLayoutPanel extends GuiAbsolutePane
{
    private final GuiListView<Pair<GuiNode, Integer>> view;

    public DebugLayoutPanel()
    {
        this.view = new GuiListView<>();
        view.setWidth(125);
        view.setHeightRatio(1);
        view.setCellWidth(125);
        view.setCellHeight(BrokkGuiPlatform.getInstance().getGuiHelper().getStringHeight());
        view.setStyle("background-color: #000000 50%;");
        view.setGuiOverflow(GuiOverflowPolicy.NONE);
        view.setCellFactory(content ->
        {
            GuiListCell<Pair<GuiNode, Integer>> cell = new GuiListCell<>(view, content);
            GuiLabel label =
                    new GuiLabel(StringUtils.repeat(' ', content.getValue()) + DebugRenderer.getNodeName(content.getKey()));
            label.setExpandToText(false);
            label.setTextAlignment(RectAlignment.LEFT_CENTER);
            label.setOnHoverEvent(e ->
            {
                if(e.isEntering())
                    label.setExpandToText(true);
                else
                {
                    label.setExpandToText(false);
                    label.setWidth(view.getWidth());
                }
            });
            cell.setGraphic(label);
            return cell;
        });

        this.addChild(view, 0, 0);
    }

    public void updateLayout(IGuiWindow window)
    {
        List<Pair<GuiNode, Integer>> flat = getFlattenedHierarchy(window.getMainPanel(), 0,
                new ArrayList<>());
        view.setElements(flat);
    }

    private List<Pair<GuiNode, Integer>> getFlattenedHierarchy(GuiFather node, int depth,
                                                               List<Pair<GuiNode, Integer>> list)
    {
        for (GuiNode children : node.getChildrens())
        {
            list.add(Pair.of(children, depth));
            if (children instanceof GuiFather)
                getFlattenedHierarchy((GuiFather) children, depth + 1, list);
        }
        return list;
    }
}
