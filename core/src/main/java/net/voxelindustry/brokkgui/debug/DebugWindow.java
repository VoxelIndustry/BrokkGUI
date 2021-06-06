package net.voxelindustry.brokkgui.debug;

import net.voxelindustry.brokkcolor.Color;
import net.voxelindustry.brokkgui.BrokkGuiPlatform;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.component.impl.Transform;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.debug.hierarchy.AccordionItem;
import net.voxelindustry.brokkgui.debug.hierarchy.AccordionLayout;
import net.voxelindustry.brokkgui.immediate.ImmediateWindow;
import net.voxelindustry.brokkgui.immediate.InteractionResult;
import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.internal.PopupHandler;
import net.voxelindustry.brokkgui.internal.profiler.IProfiler;
import net.voxelindustry.brokkgui.profiler.GuiProfiler;
import net.voxelindustry.brokkgui.style.IStyleRoot;
import net.voxelindustry.brokkgui.style.StyleComponent;
import net.voxelindustry.brokkgui.style.StyleEngine;
import net.voxelindustry.brokkgui.style.StylesheetManager;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.window.BrokkGuiScreen;
import net.voxelindustry.brokkgui.window.IGuiWindow;
import net.voxelindustry.brokkgui.window.InputType;
import net.voxelindustry.brokkgui.window.SubGuiScreen;
import org.apache.commons.lang3.tuple.Pair;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.BiPredicate;

import static com.google.common.base.Predicates.not;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.Collectors.joining;
import static net.voxelindustry.brokkgui.debug.DebugRenderer.getNodeName;
import static net.voxelindustry.brokkgui.util.ListUtil.tryGetIndex;
import static net.voxelindustry.brokkgui.util.MathUtils.clamp;

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

    private static final StyleType MENU             = StyleType.of("menu");
    private static final StyleType MENU_DARKER      = StyleType.combine(MENU, StyleType.DARK);
    private static final StyleType INVISIBLE        = StyleType.of("invisible");
    private static final StyleType INVISIBLE_PARENT = StyleType.combine(INVISIBLE, StyleType.DARK);
    private static final StyleType LOCKED_MOUSE     = StyleType.of("mouse-lock");
    private static final StyleType BUTTON_ACTION    = StyleType.of("action");

    private final IGuiWindow window;

    private Transform hoveredNode;
    private Transform selectedNode;
    private Transform hoveredHierarchyNode;

    private boolean drawHotspots;

    private boolean isInputLocked;
    private float   lockedMouseX;
    private float   lockedMouseY;

    private List<Transform> hiddenNodes = new ArrayList<>();

    private Map<String, DebugHierarchy> hierarchiesByName = new LinkedHashMap<>();

    public DebugWindow(IGuiWindow window)
    {
        this.window = window;

        addStyleSheet("/assets/brokkgui/css/debug-window.css");
    }

    @Override
    public void immediateRender()
    {
        if (!(window instanceof BrokkGuiScreen))
            return;

        handleInputLock();
        hoveredNode = getDeepestHoveredNode(((BrokkGuiScreen) window).root().transform(), getMouseX(), getMouseY(), 0).getKey();
        hoveredHierarchyNode = null;

        // WINDOWS
        box(0, 0, 120, getScreenHeight());

        String mainWindowName = window.getClass().getSimpleName();

        List<String> windowsThisFrame = new ArrayList<>();
        windowsThisFrame.add("Popups");
        windowsThisFrame.add(mainWindowName);

        hierarchiesByName.putIfAbsent(mainWindowName, new DebugHierarchy(mainWindowName, 0, false, 0));
        hierarchiesByName.putIfAbsent("Popups", new DebugHierarchy("Popups", 0, false,
                getScreenHeight() - ((BrokkGuiScreen) window).getSubGuis().size() * 14 - 14
                        - max(PopupHandler.getInstance(window).getPopups().stream().filter(GuiElement.class::isInstance).count(), 3) * 14));

        List<AccordionItem> items = new ArrayList<>();
        items.add(hierarchiesByName.get(mainWindowName));
        items.add(hierarchiesByName.get("Popups"));


        int subWindowIndex = 0;
        for (SubGuiScreen subWindow : ((BrokkGuiScreen) window).getSubGuis())
        {
            String subWindowName = subWindow.getClass().getSimpleName();
            windowsThisFrame.add(subWindowName);

            if (!hierarchiesByName.containsKey(subWindowName))
            {
                AccordionItem aboveItem = items.get(items.size() - 1);
                if (aboveItem.isCollapsed())
                    AccordionLayout.moveUp(aboveItem.getHeaderSize(), aboveItem, items);
            }
            items.add(hierarchiesByName.putIfAbsent(subWindowName, new DebugHierarchy(subWindowName, 0, true,
                    getScreenHeight() - (((BrokkGuiScreen) window).getSubGuis().size() - subWindowIndex) * 14)));
            subWindowIndex++;
        }
        hierarchiesByName.keySet().removeIf(not(windowsThisFrame::contains));

        drawWindowHierarchy(mainWindowName,
                hierarchiesByName.get(mainWindowName),
                items,
                getChildCountDeep(((BrokkGuiScreen) window).root().transform()),
                ((BrokkGuiScreen) window).root().transform());

        drawWindowHierarchy(
                "Popups",
                hierarchiesByName.get("Popups"),
                items,
                PopupHandler.getInstance(window).getPopups()
                        .stream().mapToInt(popup -> popup instanceof GuiElement ? getChildCountDeep(((GuiElement) popup).transform()) + 1 : 1)
                        .sum(),
                PopupHandler.getInstance(window).getPopups().stream()
                        .filter(GuiElement.class::isInstance)
                        .map(GuiElement.class::cast)
                        .map(GuiElement::transform)
                        .toArray(Transform[]::new));

        for (SubGuiScreen subWindow : ((BrokkGuiScreen) window).getSubGuis())
        {
            String subWindowName = subWindow.getClass().getSimpleName();
            DebugHierarchy subWindowHierarchy = hierarchiesByName.get(subWindowName);

            drawWindowHierarchy(subWindowName,
                    subWindowHierarchy,
                    items,
                    getChildCountDeep(subWindow.transform()),
                    subWindow.transform());
        }

        if (selectedNode != null)
        {
            drawNodeInfos(selectedNode);
            drawSelectedNodeInfos(selectedNode);
        }
        if (hoveredNode != null)
            drawNodeInfos(hoveredNode);
        if (hoveredHierarchyNode != null)
            drawNodeInfos(hoveredHierarchyNode);

        IProfiler profiler = BrokkGuiPlatform.getInstance().getProfiler();
        if (profiler instanceof GuiProfiler)
            drawProfilerBox((GuiProfiler) profiler);

        if (button("RELOAD CSS", 120, 0, BUTTON_ACTION).isClicked())
        {
            StylesheetManager.getInstance().forceReload((IStyleRoot) window);
            StyleEngine.refreshHierarchy(((BrokkGuiScreen) window).root().transform());

            StylesheetManager.getInstance().forceReload(this, false);
            refreshStyle();
        }
    }

    private void drawWindowHierarchy(String name, DebugHierarchy hierarchy, List<AccordionItem> accordionItems, int childCount, Transform... rootNodes)
    {
        float headerOffset = 14;
        float startY = hierarchy.getHeaderPos();

        char stateChar = !hierarchy.isCollapsed() ? '-' : '+';
        if (button(stateChar + " " + name + " (" + childCount + ") " + stateChar, 0, startY, 120, headerOffset, MENU).isClicked())
        {
            hierarchy.setCollapsed(!hierarchy.isCollapsed());
            if (!hierarchy.isCollapsed())
            {
                hierarchy.setScrollY(0);
                AccordionLayout.openItem(hierarchy, accordionItems, getScreenHeight());
            }
            else
            {
                AccordionLayout.closeItem(hierarchy, accordionItems, getScreenHeight());
            }
        }

        if (hierarchy.isCollapsed())
            return;

        float maxY = tryGetIndex(accordionItems, accordionItems.indexOf(hierarchy) + 1).map(AccordionItem::getHeaderPos).orElse((float) getScreenHeight());

        scissor(0, startY + headerOffset, 120, maxY);
        float hierarchyLength = drawHierarchy(hierarchy.getScrollY() + startY, rootNodes);
        stopScissor();

        float height = maxY - startY - headerOffset;

        if (height > hierarchyLength)
        {
            hierarchy.setScrollY(0);
            return;
        }

        if (isAreaWheeled(0, startY + headerOffset, 120, maxY))
            hierarchy.setScrollY(clamp(min(height - hierarchyLength, 0), 0, (float) (hierarchy.getScrollY() + getLastWheelValue() / 20)));

        float scrollGripHeight = height / hierarchyLength * height;
        box(120 - 5,
                startY + headerOffset - 1 - (height + 2 - scrollGripHeight) * (hierarchy.getScrollY() / (hierarchyLength - height)),
                5,
                scrollGripHeight,
                MENU_DARKER);
    }

    private void handleInputLock()
    {
        if (BrokkGuiPlatform.getInstance().getKeyboardUtil().isCtrlKeyDown() && getLastKeyPressed() == BrokkGuiPlatform.getInstance().getKeyboardUtil().getKeyCode("D"))
        {
            isInputLocked = !isInputLocked;

            if (isInputLocked)
            {
                lockedMouseX = getMouseX();
                lockedMouseY = getMouseY();
            }
        }

        if (isInputLocked)
        {
            String text = "Input LOCKED (CTRL + D)";
            textBox(text, getScreenWidth() - getStringWidth(text) - 4, getScreenHeight() - getStringHeight() - 4, StyleType.NORMAL);

            box(lockedMouseX, lockedMouseY, 8, 8, LOCKED_MOUSE);
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
                TWO_DECIMAL_FORMAT.format(profiler.getFrameRenderTimePercentile(99) / 1_000_000) + " Perc99 \n" +
                "Style:\n" +
                profiler.getTotalStyleRefresh() + " refresh.";
        textBox(framesText, getScreenWidth() - getStringWidthMultiLine(framesText) - 4, getStringHeight() + 4, StyleType.NORMAL);

        String hotspotText = drawHotspots ? "Hide HotSpots" : "Show HotSpots";
        if (button(hotspotText, getScreenWidth() - getStringWidth(hotspotText) - 4, getStringHeight() + 8 + getStringHeightMultiLine(framesText), BUTTON_ACTION) == InteractionResult.CLICKED)
        {
            drawHotspots = !drawHotspots;
        }

        String flushText = "Clear Data";
        if (button(flushText, getScreenWidth() - getStringWidth(hotspotText) - 11 - getStringWidth(flushText), getStringHeight() + 8 + getStringHeightMultiLine(framesText), BUTTON_ACTION) == InteractionResult.CLICKED)
            profiler.clearData();

        if (drawHotspots)
        {
            List<Map.Entry<GuiElement, Double>> worstRenderTimes = profiler.getWorstRenderTimeElements(15, false);

            worstRenderTimes.forEach(element ->
            {
                textBox(TWO_DECIMAL_FORMAT.format(element.getValue() / 1_000_000F) + "ms", element.getKey().getLeftPos(), element.getKey().getBottomPos(), StyleType.NORMAL);
            });
        }
    }

    private float drawHierarchy(float startY, Transform... nodes)
    {
        int addedHeight = 0;
        for (Transform node : nodes)
        {
            addedHeight += drawHierarchyOfNode(node, 0, addedHeight, startY);
        }

        return addedHeight * getStringHeight();
    }

    private int drawHierarchyOfNode(Transform node, int depth, int height, float startY)
    {
        int addedHeight = 0;
        boolean isHidden = hiddenNodes.contains(node);

        float currentY = 16 + (height + addedHeight) * getStringHeight() + startY;
        String nodeName = getNodeName(node.element());
        InteractionResult nodeClick = button(nodeName,
                9 + depth * 5,
                currentY,
                getNodeNameStyle(node));

        if (nodeClick.isClicked())
            selectedNode = node;

        if (isAreaHovered(0,
                16 + (height + addedHeight) * getStringHeight() + startY,
                100,
                16 + (height + addedHeight + 1) * getStringHeight() + startY))
            hoveredHierarchyNode = node;

        addedHeight++;

        if (node.childCount() != 0 && button(isHidden ? "+" : "-", 2, currentY, getNodeNameStyle(node)).isClicked())
        {
            if (hiddenNodes.contains(node))
                hiddenNodes.remove(node);
            else
                hiddenNodes.add(node);
        }

        if (isHidden)
            text("(" + getChildCountDeep(node) + ")", 11 + depth * 5 + getStringWidth(nodeName), currentY, getNodeNameStyle(node));
        else
        {
            for (Transform child : node.children())
                addedHeight += drawHierarchyOfNode(child, depth + 1, height + addedHeight, startY);
        }
        return addedHeight;
    }

    private void drawNodeInfos(Transform node)
    {
        emptyBox(node.leftPos(), node.topPos(), node.width(), node.height(), StyleType.NORMAL);

        StringJoiner builder = new StringJoiner("\n");

        String nodeName = getNodeName(node.element()) + node.element().get(StyleComponent.class).styleClass().getValue().stream().map(str -> " ." + str).collect(joining());
        builder.add(nodeName);

        builder.add("x: " + NO_DECIMAL_FORMAT.format(node.leftPos()) + " y: " + NO_DECIMAL_FORMAT.format(node.topPos()) + " w: " + NO_DECIMAL_FORMAT.format(node.width()) + " h: " + NO_DECIMAL_FORMAT.format(node.height()));
        builder.add("visible: " + isNodeVisible(node));

        if (node.element().has(TextComponent.class))
        {
            RectBox padding = node.element().get(TextComponent.class).computedTextPadding();
            builder.add("Padding: Up=" + NO_DECIMAL_FORMAT.format(padding.getTop()) + ", Down=" + NO_DECIMAL_FORMAT.format(padding.getBottom()) + ", Left=" + NO_DECIMAL_FORMAT.format(padding.getLeft()) + ", Right=" + NO_DECIMAL_FORMAT.format(padding.getRight()));
        }

        textBox(builder.toString(), node.leftPos(), node.bottomPos(), StyleType.NORMAL);
    }

    private void drawSelectedNodeInfos(Transform node)
    {
        if (node.element().has(TextComponent.class))
        {
            RectBox padding = node.element().get(TextComponent.class).computedTextPadding();
            box(node.leftPos(), node.topPos(), padding.getLeft(), node.height(), Color.RED.addAlpha(-0.5F), Color.RED.addAlpha(-0.15F), 0.5F, Color.RED.addAlpha(-0.5F), Color.RED.addAlpha(-0.3F));
            box(node.leftPos(), node.topPos(), node.width(), padding.getTop(), Color.AQUA.addAlpha(-0.5F), Color.AQUA.addAlpha(-0.15F), 0.5F, Color.AQUA.addAlpha(-0.5F), Color.AQUA.addAlpha(-0.3F));
            box(node.rightPos() - padding.getRight(), node.topPos(), padding.getRight(), node.height(), Color.YELLOW.addAlpha(-0.5F), Color.YELLOW.addAlpha(-0.15F), 0.5F, Color.YELLOW.addAlpha(-0.5F), Color.YELLOW.addAlpha(-0.3F));
            box(node.leftPos(), node.bottomPos() - padding.getBottom(), node.width(), padding.getBottom(), Color.GREEN.addAlpha(-0.5F), Color.GREEN.addAlpha(-0.15F), 0.5F, Color.GREEN.addAlpha(-0.5F), Color.GREEN.addAlpha(-0.3F));
        }
    }

    private Pair<Transform, Integer> getDeepestHoveredNode(Transform current, float mouseX, float mouseY, int depth)
    {
        if (!current.childrenProperty().isEmpty())
        {
            Pair<Transform, Integer> deepest = null;

            for (Transform child : current.childrenProperty().getModifiableValue())
            {
                if (!child.isPointInside(mouseX, mouseY))
                    continue;

                Pair<Transform, Integer> candidate = getDeepestHoveredNode(child, mouseX, mouseY, depth + 1);

                if (deepest == null || candidate.getRight() > deepest.getRight())
                    deepest = candidate;
            }
            if (deepest != null)
                return deepest;
        }
        return Pair.of(current.isPointInside(mouseX, mouseY) ? current : null, depth);
    }

    private int getChildCountDeep(Transform node)
    {
        if (node.childCount() == 0)
            return 0;
        return node.childCount() + node.streamChildren().mapToInt(this::getChildCountDeep).sum();
    }

    private StyleType getNodeNameStyle(Transform node)
    {
        if (!node.element().isVisible())
            return INVISIBLE;
        if (!isNodeVisible(node))
            return INVISIBLE_PARENT;
        return StyleType.NORMAL;
    }

    private boolean isNodeVisible(Transform node)
    {
        if (!node.element().isVisible())
            return false;
        if (node.parent() != null)
            return isNodeVisible(node.parent());
        return true;
    }

    // Input filtering to take control of the mouse
    @Override
    public boolean test(IGuiWindow window, InputType inputType)
    {
        if (isInputLocked)
            return window == this;
        return true;
    }
}
