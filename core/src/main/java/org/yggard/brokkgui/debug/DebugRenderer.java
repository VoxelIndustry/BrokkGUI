package org.yggard.brokkgui.debug;

import org.apache.commons.lang3.StringUtils;
import org.yggard.brokkgui.BrokkGuiPlatform;
import org.yggard.brokkgui.component.GuiNode;
import org.yggard.brokkgui.control.GuiFather;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.gui.SubGuiScreen;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.ColorConstants;
import org.yggard.brokkgui.shape.GuiShape;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

public class DebugRenderer
{
    private static final NumberFormat FORMAT;

    static
    {
        FORMAT = NumberFormat.getInstance();
        FORMAT.setMinimumFractionDigits(0);
    }

    private final BrokkGuiScreen screen;
    private final Color          borderColor = ColorConstants.getColor("steelblue");
    private final Color          textColor   = ColorConstants.getColor("gold");
    private final Color          boxColor    = Color.BLACK.addAlpha(-0.5f);

    private final Set<GuiFather> openedNodes;

    private GuiFather openedRoot;
    private GuiNode   hoveredNode;
    private int       mouseX, mouseY;

    private float accordionScrollX, accordionScrollY;

    private IGuiRenderer renderer;

    public DebugRenderer(BrokkGuiScreen screen)
    {
        this.screen = screen;

        this.openedNodes = new HashSet<>();
    }

    public void render(IGuiRenderer renderer, int mouseX, int mouseY)
    {
        this.renderer = renderer;
        this.hoveredNode = null;
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        this.drawTextBox(renderer, getScreenText(screen), screen.getxPos(),
                screen.getyPos() - renderer.getHelper().getStringHeight());
        renderer.getHelper().drawColoredEmptyRect(renderer, screen.getxPos(), screen.getyPos(), screen.getWidth(),
                screen.getHeight(), 300, borderColor, 0.5f);

        if (screen.getMainPanel() == null)
            return;

        if (openedRoot == null)
        {
            this.drawTextBox(renderer, "MainPanel+", 1, 1);

            float x = 1 + renderer.getHelper().getStringWidth("MainPanel+") + 2;
            for (SubGuiScreen subGui : screen.getSubGuis())
            {
                this.drawTextBox(renderer, subGui.getClass().getSimpleName() + "+", x, 1);
                x += renderer.getHelper().getStringWidth(subGui.getClass().getSimpleName() + "+") + 2;
            }
            return;
        }

        this.drawTextBox(renderer, getNodeText(openedRoot), openedRoot.getxPos() + openedRoot.getxTranslate(),
                openedRoot.getyPos() + openedRoot.getyTranslate() + openedRoot.getHeight());
        this.drawAccordion(renderer, openedRoot, "Root", accordionScrollX, accordionScrollY);

        if (hoveredNode != null)
        {
            this.drawTextBox(renderer, getNodeText(hoveredNode), hoveredNode.getxPos() + hoveredNode.getxTranslate(),
                    hoveredNode.getyPos() + hoveredNode.getyTranslate() + hoveredNode.getHeight());
            renderer.getHelper().drawColoredEmptyRect(renderer, hoveredNode.getxPos() + hoveredNode.getxTranslate(),
                    hoveredNode.getyPos() + hoveredNode.getyTranslate(), hoveredNode.getWidth(),
                    hoveredNode.getHeight(), 300, borderColor, 0.5f);
        }
    }

    public void onClick(int mouseX, int mouseY)
    {
        if (this.openedRoot == null)
        {
            if (mouseY > 11)
                return;

            if (mouseX < renderer.getHelper().getStringWidth("MainPanel+") + 1)
                openedRoot = screen.getMainPanel();
            else
            {
                float x = 1 + renderer.getHelper().getStringWidth("MainPanel+") + 2;
                for (SubGuiScreen subGui : screen.getSubGuis())
                {
                    x += renderer.getHelper().getStringWidth(subGui.getClass().getSimpleName() + "+") + 2;

                    if (mouseX < x)
                        this.openedRoot = subGui;
                }
            }
        }
        else if (mouseX < 100 && mouseY < 11)
        {
            this.openedRoot = null;
            this.accordionScrollX = 0;
            this.accordionScrollY = 0;
        }
    }

    public void onMouseInput()
    {
        float scrolled = BrokkGuiPlatform.getInstance().getMouseUtil().getEventDWheel() / 10f;
        boolean vertical = !BrokkGuiPlatform.getInstance().getKeyboardUtil().isShiftKeyDown();

        if (vertical)
        {
            if (accordionScrollY + scrolled > 0)
                return;
            accordionScrollY += scrolled;
        }
        else
        {
            if (accordionScrollX + scrolled > 0)
                return;
            accordionScrollX += scrolled;
        }
    }

    private void drawAccordion(IGuiRenderer renderer, GuiFather root, String rootName, float startX, float startY)
    {
        this.openedNodes.add(root);

        renderer.getHelper().drawColoredRect(renderer, startX, 0, startX + 100, 1500, 400, boxColor);
        renderer.getHelper().drawString(rootName, startX + 0.5f, startY + 1, 300, textColor, Color.ALPHA);

        this.drawAccordionLevel(renderer, root, startX, startY + renderer.getHelper().getStringHeight() + 2, 0);
    }

    private int drawAccordionLevel(IGuiRenderer renderer, GuiFather parent, float startX, float startY, int depth)
    {
        int height = 0;

        for (GuiNode child : parent.getChildrens())
        {
            if (mouseX > startX + 5 * depth && mouseX < startX + 5 * depth + renderer.getHelper().getStringWidth(child.getClass().getSimpleName()) &&
                    mouseY > startY + height && mouseY < startY + height + renderer.getHelper().getStringHeight())
                hoveredNode = child;

            renderer.getHelper().drawString(child.getClass().getSimpleName(), startX + 5 * depth, startY + height,
                    300, textColor, Color.ALPHA);
            height += renderer.getHelper().getStringHeight() + 1;

            if (child instanceof GuiFather)
                height += this.drawAccordionLevel(renderer, (GuiFather) child, startX, startY + height, depth + 1);
        }
        return height;
    }

    private String getScreenText(BrokkGuiScreen screen)
    {
        return screen.getClass().getSimpleName() + " x:" + FORMAT.format(screen.getxPos()) + " y:" + FORMAT.format(screen.getyPos()) +
                " w:" + FORMAT.format(screen.getWidth()) + " h:" + FORMAT.format(screen.getHeight());
    }

    private String getNodeText(GuiNode node)
    {
        StringBuilder builder = new StringBuilder();

        if (node instanceof GuiShape)
        {
            if (!StringUtils.isEmpty(node.getID()))
                builder.append("#").append(node.getID());
            node.getStyleClass().getValue().forEach(str -> builder.append(" .").append(str));
        }
        else
            builder.append(node.getClass().getSimpleName());
        builder.append(" x:").append(FORMAT.format(node.getxPos()));
        builder.append(" y:").append(FORMAT.format(node.getyPos()));
        builder.append(" w:").append(FORMAT.format(node.getWidth()));
        builder.append(" h:").append(FORMAT.format(node.getHeight()));
        builder.append(" z:").append(FORMAT.format(node.getzLevel()));
        builder.append(" v:").append(node.isVisible());

        return builder.toString();
    }

    private void drawTextBox(IGuiRenderer renderer, String text, float startX, float startY)
    {
        renderer.getHelper().drawColoredRect(renderer, startX, startY, renderer.getHelper().getStringWidth(text),
                renderer.getHelper().getStringHeight(), 300, boxColor);
        renderer.getHelper().drawString(text, startX + 0.5f, startY + 1.25f, 300, textColor, Color.ALPHA);
    }
}
