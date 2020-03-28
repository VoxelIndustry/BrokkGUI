package net.voxelindustry.brokkgui.immediate;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.voxelindustry.brokkgui.data.RectBox;
import net.voxelindustry.brokkgui.immediate.element.BoxElement;
import net.voxelindustry.brokkgui.immediate.element.ButtonElement;
import net.voxelindustry.brokkgui.immediate.element.ImmediateWindowBridge;
import net.voxelindustry.brokkgui.immediate.element.TextBoxElement;
import net.voxelindustry.brokkgui.immediate.element.TextElement;
import net.voxelindustry.brokkgui.immediate.style.ReducedStyleList;
import net.voxelindustry.brokkgui.immediate.style.StyleType;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.style.IStyleRoot;
import net.voxelindustry.brokkgui.style.StylesheetManager;
import net.voxelindustry.brokkgui.style.adapter.IStyleDecoder;
import net.voxelindustry.brokkgui.style.adapter.StyleTranslator;
import net.voxelindustry.brokkgui.style.tree.StyleList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ImmediateWindow extends BaseImmediateWindow implements ImmediateWindowBridge, IStyleRoot,
        BoxElement, TextElement, TextBoxElement, ButtonElement
{
    private Table<Class<?>, StyleType, Object> elementStyles = HashBasedTable.create();

    private ReducedStyleList styleList;
    private List<String>     styleSheets = new ArrayList<>();

    private Map<String, IStyleDecoder<?>> styleDecodersByRules = new HashMap<>();

    public void registerStyleRule(String rule, Class<?> ruleClass)
    {
        this.registerStyleRule(rule, StyleTranslator.getInstance().getDecoder(ruleClass));
    }

    public void registerStyleRule(String rule, IStyleDecoder<?> decoder)
    {
        this.styleDecodersByRules.put(rule, decoder);
    }

    public void addStyleSheet(String styleSheetPath)
    {
        this.styleSheets.add(styleSheetPath);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        registerStyleRule("color", Color.class);
        registerStyleRule("background-color", Color.class);
        registerStyleRule("border-color", Color.class);
        registerStyleRule("shadow-color", Color.class);

        registerStyleRule("border-width", Float.class);
        registerStyleRule("padding", RectBox.class);

        StylesheetManager.getInstance().refreshStylesheets(this, false);
    }

    @Override
    public String getThemeID()
    {
        return "immediate";
    }

    @Override
    public List<String> getStylesheets()
    {
        return styleSheets;
    }

    @Override
    public void setStyleList(StyleList list)
    {
        styleList = new ReducedStyleList(list, styleDecodersByRules);
    }

    @Override
    public <T> void setStyleObject(T object, StyleType type)
    {
        this.elementStyles.put(object.getClass(), type, object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getStyleObject(StyleType type, Class<T> objectClass)
    {
        return (T) this.elementStyles.get(objectClass, type);
    }

    @Override
    public <T> T getStyleValue(String element, StyleType styleType, String pseudoClass, String rule, T defaultValue)
    {
        T styleValue = styleList.getStyleValue(element, styleType, pseudoClass, rule);

        if (styleValue == null)
            return defaultValue;
        return styleValue;
    }

    @Override
    public boolean isAreaHovered(float startX, float startY, float endX, float endY)
    {
        if (isScissorActive())
        {
            if (!getScissorBox().isInside(getMouseX(), getMouseY()))
                return false;
        }
        return getMouseX() > startX && getMouseX() < endX && getMouseY() > startY && getMouseY() < endY;
    }

    @Override
    public boolean isAreaClicked(float startX, float startY, float endX, float endY)
    {
        if (isScissorActive())
        {
            if (!getScissorBox().isInside(getLastClickX(), getLastClickY()))
                return false;
        }
        return getLastClickX() > startX && getLastClickX() < endX && getLastClickY() > startY && getLastClickY() < endY;
    }

    @Override
    public boolean isAreaWheeled(float startX, float startY, float endX, float endY)
    {
        if (isScissorActive())
        {
            if (!getScissorBox().isInside(getLastWheelX(), getLastWheelY()))
                return false;
        }
        return getLastWheelX() > startX && getLastWheelX() < endX && getLastWheelY() > startY && getLastWheelY() < endY;
    }
}
