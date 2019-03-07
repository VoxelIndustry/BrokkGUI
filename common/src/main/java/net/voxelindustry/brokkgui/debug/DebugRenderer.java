package net.voxelindustry.brokkgui.debug;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.gui.SubGuiScreen;
import net.voxelindustry.brokkgui.internal.IGuiRenderer;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.ColorConstants;
import net.voxelindustry.brokkgui.util.MathUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

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
    private final Color          borderColor      = ColorConstants.getColor("steelblue");
    private final Color          textColor        = ColorConstants.getColor("gold");
    private final Color          hoveredTextColor = ColorConstants.getColor("gold").shade(0.2f);
    private final Color          boxColor         = Color.BLACK.addAlpha(-0.5f);

    private final Set<GuiFather> openedNodes;

    private GuiFather openedRoot;
    private GuiNode   hoveredNode;
    private GuiNode   castHoveredNode;
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

        this.drawSelectedNodeBoxes(renderer, hoveredNode);

        if (!screen.getMainPanel().isPointInside(mouseX, mouseY))
            return;

        this.castHoveredNode = this.getDeepestHoveredNode(screen.getMainPanel(), mouseX, mouseY, 0).getKey();
        this.drawSelectedNodeBoxes(renderer, castHoveredNode);
    }

    private void drawSelectedNodeBoxes(IGuiRenderer renderer, GuiNode node)
    {
        if (node != null)
        {
            float xStart = MathUtils.clamp(0,
                    screen.getScreenWidth() - renderer.getHelper().getStringWidth(getNodeText(node)),
                    node.getxPos() + node.getxTranslate());
            float yStart = MathUtils.clamp(0, screen.getScreenHeight() - renderer.getHelper().getStringHeight(),
                    node.getyPos() + node.getyTranslate() + node.getHeight());

            this.drawTextBox(renderer, getNodeText(node), xStart, yStart);
            renderer.getHelper().drawColoredEmptyRect(renderer, node.getxPos() + node.getxTranslate(),
                    node.getyPos() + node.getyTranslate(), node.getWidth(),
                    node.getHeight(), 300, borderColor, 0.5f);
        }
    }

    private Pair<GuiNode, Integer> getDeepestHoveredNode(GuiNode current, int mouseX, int mouseY, int depth)
    {
        if (current instanceof GuiFather)
        {
            Pair<GuiNode, Integer> deepest = null;

            for (GuiNode child : ((GuiFather) current).getChildrens())
            {
                if (!child.isPointInside(mouseX, mouseY))
                    continue;

                Pair<GuiNode, Integer> candidate = getDeepestHoveredNode(child, mouseX, mouseY, depth + 1);

                if (deepest == null || candidate.getRight() > deepest.getRight())
                    deepest = candidate;
            }
            if (deepest != null)
                return deepest;
        }
        return Pair.of(current, depth);
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
            String childName = getNodeName(child);

            if (mouseX > startX + 5 * depth && mouseX < startX + 5 * depth + renderer.getHelper().getStringWidth(childName) &&
                    mouseY > startY + height && mouseY < startY + height + renderer.getHelper().getStringHeight())
                hoveredNode = child;

            renderer.getHelper().drawString(childName, startX + 5 * depth, startY + height,
                    300, hoveredNode == child || castHoveredNode == child ? hoveredTextColor : textColor, Color.ALPHA);
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

    private String getNodeName(GuiNode node)
    {
        StringBuilder builder = new StringBuilder();

        if (!StringUtils.isEmpty(node.getID()))
            builder.append("#").append(node.getID());
        else
            builder.append(node.getClass().getSimpleName());

        return builder.toString();
    }

    private String getNodeText(GuiNode node)
    {
        StringBuilder builder = new StringBuilder();

        builder.append(getNodeName(node));
        node.getStyleClass().getValue().forEach(str -> builder.append(" .").append(str));
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
