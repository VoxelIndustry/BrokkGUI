package net.voxelindustry.brokkgui.debug;

import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiNode;
import net.voxelindustry.brokkgui.control.GuiFather;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.gui.IGuiWindow;
import net.voxelindustry.brokkgui.gui.InputType;
import net.voxelindustry.brokkgui.immediate.ImmediateWindow;
import net.voxelindustry.brokkgui.immediate.InteractionResult;
import net.voxelindustry.brokkgui.immediate.style.BoxStyle;
import net.voxelindustry.brokkgui.immediate.style.ButtonStyle;
import net.voxelindustry.brokkgui.immediate.style.EmptyBoxStyle;
import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.immediate.style.TextBoxStyle;
import net.voxelindustry.brokkgui.immediate.style.TextStyle;
import net.voxelindustry.brokkgui.internal.PopupHandler;
import net.voxelindustry.brokkgui.internal.profiler.GuiProfiler;
import net.voxelindustry.brokkgui.internal.profiler.IProfiler;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.ColorConstants;
import net.voxelindustry.brokkgui.util.MathUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.BiPredicate;

import static com.google.common.base.Predicates.not;
import static java.lang.Math.signum;
import static java.util.stream.Collectors.joining;
import static net.voxelindustry.brokkgui.debug.DebugRenderer.getNodeName;

public class DebugWindow extends ImmediateWindow implements BiPredicate<IGuiWindow, InputType>
{
    private static final NumberFormat NO_DECIMAL_FORMAT;
    private static final NumberFormat TWO_DECIMAL_FORMAT;

    static
    {
        NO_DECIMAL_FORMAT = NumberFormat.getInstance();
        NO_DECIMAL_FORMAT.setMinimumFractionDigits(0);

        TWO_DECIMAL_FORMAT = NumberFormat.getInstance();
        TWO_DECIMAL_FORMAT.setMinimumFractionDigits(2);
        TWO_DECIMAL_FORMAT.setMaximumFractionDigits(2);
    }

    public static final Color BORDER_COLOR = ColorConstants.getColor("steelblue");

    public static final Color TEXT_COLOR         = Color.fromHex("#D4AF37");
    public static final Color HOVERED_TEXT_COLOR = Color.fromHex("#E0C56E");

    public static final Color INVISIBLE_TEXT_COLOR         = Color.GRAY.addAlpha(-0.62F).shade(0.1F);
    public static final Color INVISIBLE_HOVERED_TEXT_COLOR = Color.LIGHT_GRAY.addAlpha(-0.62F).shade(0.1F);

    public static final Color BOX_COLOR           = Color.BLACK.addAlpha(-0.5f);
    public static final Color SELECTION_BOX_COLOR = Color.AQUA;

    private static final BoxStyle LOCKED_MOUSE_STYLE = BoxStyle.build()
            .setBoxColor(Color.AQUA.addAlpha(-0.6F))
            .setBorderColor(Color.AQUA.addAlpha(-0.4F))
            .setBorderThin(1)
            .create();

    private static final TextStyle INVISIBLE_NODE_STYLE = TextStyle.build()
            .setTextColor(INVISIBLE_TEXT_COLOR)
            .setHoverTextColor(INVISIBLE_HOVERED_TEXT_COLOR)
            .create();

    private static final TextStyle INVISIBLE_PARENT_NODE_STYLE = TextStyle.build()
            .setTextColor(INVISIBLE_TEXT_COLOR.shade(-0.2F))
            .setHoverTextColor(INVISIBLE_HOVERED_TEXT_COLOR.shade(-0.2F))
            .create();

    private static final ButtonStyle INVISIBLE_NODE_BUTTON_STYLE = ButtonStyle.build()
            .setTextColor(INVISIBLE_TEXT_COLOR)
            .setHoverTextColor(INVISIBLE_HOVERED_TEXT_COLOR)
            .create();

    private static final ButtonStyle INVISIBLE_PARENT_NODE_BUTTON_STYLE = ButtonStyle.build()
            .setTextColor(INVISIBLE_TEXT_COLOR.shade(-0.2F))
            .setHoverTextColor(INVISIBLE_HOVERED_TEXT_COLOR.shade(-0.2F))
            .create();

    private static final ButtonStyle MENU_HEADER_STYLE = ButtonStyle.build()
            .setTextColor(Color.BLACK)
            .setBoxColor(Color.fromHex("#FAFAFA"))
            .setHoverBoxColor(Color.fromHex("#F5F5F5"))
            .setBorderColor(Color.fromHex("#212121"))
            .setPadding(RectBox.build().all(2).create())
            .create();

    private final IGuiWindow window;

    private GuiNode hoveredNode;
    private GuiNode selectedNode;
    private GuiNode hoveredHierarchyNode;

    private boolean isInputLocked;
    private int     lockedMouseX;
    private int     lockedMouseY;

    private List<GuiNode> hiddenNodes = new ArrayList<>();

    private Map<String, DebugHierarchy> hierarchiesByName = new HashMap<>();

    public DebugWindow(IGuiWindow window)
    {
        this.window = window;

        this.setBoxStyle(BoxStyle.build()
                .setBoxColor(BOX_COLOR)
                .setBorderColor(BORDER_COLOR)
                .create());

        this.setTextStyle(TextStyle.build()
                .setTextColor(TEXT_COLOR)
                .setHoverTextColor(HOVERED_TEXT_COLOR)
                .create());

        this.setTextStyle(TextStyle.build()
                        .setTextColor(INVISIBLE_TEXT_COLOR)
                        .setHoverTextColor(INVISIBLE_HOVERED_TEXT_COLOR)
                        .create(),
                StyleType.LIGHT);

        this.setTextBoxStyle(TextBoxStyle.build()
                .setTextColor(TEXT_COLOR)
                .setHoverTextColor(HOVERED_TEXT_COLOR)
                .setBoxColor(BOX_COLOR)
                .setBorderColor(BORDER_COLOR)
                .setPadding(RectBox.build().all(2).create())
                .create());

        this.setEmptyBoxStyle(EmptyBoxStyle.build()
                .setBorderColor(SELECTION_BOX_COLOR)
                .setBorderThin(0.5F)
                .create());

        this.setButtonStyle(ButtonStyle.build()
                .setTextColor(TEXT_COLOR)
                .setHoverTextColor(HOVERED_TEXT_COLOR)
                .create());
    }

    @Override
    public void immediateRender()
    {
        if (!(window instanceof BrokkGuiScreen))
            return;

        handleInputLock();
        hoveredNode = getDeepestHoveredNode(((BrokkGuiScreen) window).getMainPanel(), getMouseX(), getMouseY(), 0).getKey();
        hoveredHierarchyNode = null;

        // WINDOWS
        box(0, 0, 120, getScreenHeight());

        String mainWindowName = window.getClass().getSimpleName();

        List<String> windowsThisFrame = new ArrayList<>();
        windowsThisFrame.add("Popups");
        windowsThisFrame.add(mainWindowName);

        hierarchiesByName.putIfAbsent("Popups", new DebugHierarchy("Popups", 0, true));
        hierarchiesByName.putIfAbsent(mainWindowName, new DebugHierarchy(mainWindowName, 0, true));
        ((BrokkGuiScreen) window).getSubGuis().forEach(subWindow ->
        {
            String subWindowName = subWindow.getClass().getSimpleName();
            windowsThisFrame.add(subWindowName);
            hierarchiesByName.computeIfAbsent(subWindowName, name -> new DebugHierarchy(name, 0, false));
        });
        hierarchiesByName.keySet().removeIf(not(windowsThisFrame::contains));

        drawWindowHierarchy(mainWindowName,
                hierarchiesByName.get(mainWindowName),
                getChildCountDeep(((BrokkGuiScreen) window).getMainPanel()),
                0,
                getScreenHeight() / 2,
                ((BrokkGuiScreen) window).getMainPanel());
        drawWindowHierarchy(
                "Popups",
                hierarchiesByName.get("Popups"),
                PopupHandler.getInstance(window).getPopups()
                        .stream().mapToInt(popup -> popup instanceof GuiFather ? getChildCountDeep((GuiFather) popup) : 1)
                        .sum(),
                getScreenHeight() / 2,
                getScreenHeight(),
                PopupHandler.getInstance(window).getPopups().stream()
                        .filter(GuiNode.class::isInstance)
                        .map(GuiNode.class::cast)
                        .toArray(GuiNode[]::new));

        if (selectedNode != null)
            drawNodeInfos(selectedNode);
        if (hoveredNode != null)
            drawNodeInfos(hoveredNode);
        if (hoveredHierarchyNode != null)
            drawNodeInfos(hoveredHierarchyNode);

        IProfiler profiler = BrokkGuiPlatform.getInstance().getProfiler();
        if (profiler instanceof GuiProfiler)
            drawProfilerBox((GuiProfiler) profiler);
    }

    private void drawWindowHierarchy(String name, DebugHierarchy hierarchy, int childCount, float startY, float maxY, GuiNode... rootNodes)
    {
        if (hasMouseWheeledBox(0, startY + 14, 120, maxY))
            hierarchy.setScrollY(MathUtils.clamp(-1000, 0, (float) (hierarchy.getScrollY() + signum(getLastWheelValue()) * getStringHeight())));

        scissor(0, startY, 120, maxY);
        drawHierarchy(hierarchy.getScrollY() + startY, rootNodes);
        stopScissor();

        button("- " + name + " (" + childCount + ") -", 0, startY, 120, 14, MENU_HEADER_STYLE);
    }

    private void handleInputLock()
    {
        if (BrokkGuiPlatform.getInstance().getKeyboardUtil().isCtrlKeyDown() && this.getLastKeyPressed() == BrokkGuiPlatform.getInstance().getKeyboardUtil().getKeyCode("D"))
        {
            this.isInputLocked = !this.isInputLocked;

            if (this.isInputLocked)
            {
                this.lockedMouseX = getMouseX();
                this.lockedMouseY = getMouseY();
            }
        }

        if (this.isInputLocked)
        {
            String text = "Input LOCKED (CTRL + D)";
            textBox(text, getScreenWidth() - getStringWidth(text) - 4, getScreenHeight() - getStringHeight() - 4, StyleType.NORMAL);

            box(lockedMouseX, lockedMouseY, 8, 8, LOCKED_MOUSE_STYLE);
        }
        else
        {
            String text = "Input UNLOCKED (CTRL + D)";
            textBox(text, getScreenWidth() - getStringWidth(text) - 4, getScreenHeight() - getStringHeight() - 4, StyleType.NORMAL);
        }
    }

    private void drawProfilerBox(GuiProfiler profiler)
    {
        String recordsText = "Collected records: " + profiler.getRecordsCount();
        textBox(recordsText, getScreenWidth() - getStringWidth(recordsText) - 4, 0, StyleType.NORMAL);

        String framesText = "Frame render time:\n" +
                TWO_DECIMAL_FORMAT.format(profiler.getFrameRenderTimePercentile(50) / 1_000_000) + " Med " +
                TWO_DECIMAL_FORMAT.format(profiler.getFrameRenderTimeMax() / 1_000_000) + " Max\n" +
                TWO_DECIMAL_FORMAT.format(profiler.getFrameRenderTimePercentile(95) / 1_000_000) + " Perc95 " +
                TWO_DECIMAL_FORMAT.format(profiler.getFrameRenderTimePercentile(99) / 1_000_000) + " Perc99 ";
        textBox(framesText, getScreenWidth() - getStringWidthMultiLine(framesText) - 4, getStringHeight() + 6, StyleType.NORMAL);
    }

    private void drawHierarchy(float startY, GuiNode... nodes)
    {
        int addedHeight = 0;
        for (GuiNode node : nodes)
        {
            addedHeight += drawHierarchyOfNode(node, 0, addedHeight, startY);
        }
    }

    private int drawHierarchyOfNode(GuiNode node, int depth, int height, float startY)
    {
        int addedHeight = 0;
        boolean isHidden = this.hiddenNodes.contains(node);

        float currentY = 16 + (height + addedHeight) * getStringHeight() + startY;
        String nodeName = getNodeName(node);
        InteractionResult nodeClick = button(nodeName,
                9 + depth * 5,
                currentY,
                getNodeButtonStyle(node));

        if (nodeClick.isClicked())
            selectedNode = node;

        if (isMouseOverBox(0,
                16 + (height + addedHeight) * getStringHeight() + startY,
                100,
                16 + (height + addedHeight + 1) * getStringHeight() + startY))
            hoveredHierarchyNode = node;

        addedHeight++;

        if (node instanceof GuiFather)
        {
            if (((GuiFather) node).getChildCount() != 0 && button(isHidden ? "+" : "-", 2, currentY, getNodeButtonStyle(node)).isClicked())
            {
                if (hiddenNodes.contains(node))
                    hiddenNodes.remove(node);
                else
                    hiddenNodes.add(node);
            }

            if (isHidden)
                text("(" + getChildCountDeep((GuiFather) node) + ")", 11 + depth * 5 + getStringWidth(nodeName), currentY, getNodeNameStyle(node));
            else
            {
                for (GuiNode child : ((GuiFather) node).getChildrens())
                    addedHeight += drawHierarchyOfNode(child, depth + 1, height + addedHeight, startY);
            }
        }
        return addedHeight;
    }

    private void drawNodeInfos(GuiNode node)
    {
        emptyBox(node.getLeftPos(), node.getTopPos(), node.getWidth(), node.getHeight(), StyleType.NORMAL);

        StringJoiner builder = new StringJoiner("\n");

        String nodeName = getNodeName(node) + node.getStyleClass().getValue().stream().map(str -> " ." + str).collect(joining());
        builder.add(nodeName);

        builder.add("x: " + NO_DECIMAL_FORMAT.format(node.getLeftPos()) + " y: " + NO_DECIMAL_FORMAT.format(node.getTopPos()) + " w: " + NO_DECIMAL_FORMAT.format(node.getWidth()) + " h: " + NO_DECIMAL_FORMAT.format(node.getHeight()));
        builder.add("visible: " + isNodeVisible(node));

        textBox(builder.toString(), node.getLeftPos(), node.getBottomPos(), StyleType.NORMAL);

/*                List<String> styleText = new ArrayList<>();
            for (Map.Entry<String, StyleProperty<?>> entry : node.getStyle().getProperties().entrySet())
            {
                styleText.add(entry.getKey() + ": " + (entry.getValue().getValue() != null ?
                        StyleTranslator.getInstance().encode(entry.getValue().getValue(),
                                entry.getValue().getValueClass(), true) : "null"));
            }

            float styleWidth = (float) styleText.stream().mapToDouble(getRenderer().getHelper()::getStringWidth).max().orElse(0);
            float styleHeight = styleText.size() * getStringHeight();
            float currentY = 2;

            box(child.getLeftPos(), child.getBottomPos(), styleWidth + 4, styleHeight + 4, StyleType.NORMAL);
            for (String line : styleText)
            {
                text(line, child.getLeftPos() + 2, child.getBottomPos() + currentY, StyleType.NORMAL);
                currentY += getStringHeight();
            }*/
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
        return Pair.of(current.isPointInside(mouseX, mouseY) ? current : null, depth);
    }

    private int getChildCountDeep(GuiFather node)
    {
        return node.getChildCount() + node.getChildrens().stream().mapToInt(child -> child instanceof GuiFather ? getChildCountDeep((GuiFather) child) : 0).sum();
    }

    private TextStyle getNodeNameStyle(GuiNode node)
    {
        if (!node.isVisible())
            return INVISIBLE_NODE_STYLE;
        if (!isNodeVisible(node))
            return INVISIBLE_PARENT_NODE_STYLE;
        return getStyleObject(StyleType.NORMAL, TextStyle.class);
    }

    private ButtonStyle getNodeButtonStyle(GuiNode node)
    {
        if (!node.isVisible())
            return INVISIBLE_NODE_BUTTON_STYLE;
        if (!isNodeVisible(node))
            return INVISIBLE_PARENT_NODE_BUTTON_STYLE;
        return getStyleObject(StyleType.NORMAL, ButtonStyle.class);
    }

    private boolean isNodeVisible(GuiNode node)
    {
        if (!node.isVisible())
            return false;
        if (node.getFather() != null)
            return isNodeVisible(node.getFather());
        return true;
    }

    // Input filtering to take control of the mouse
    @Override
    public boolean test(IGuiWindow window, InputType inputType)
    {
        if (this.isInputLocked)
            return window == this;
        return true;
    }
}
