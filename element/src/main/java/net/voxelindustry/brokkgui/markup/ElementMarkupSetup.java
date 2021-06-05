package net.voxelindustry.brokkgui.markup;

import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.element.GuiLink;
import net.voxelindustry.brokkgui.element.input.GuiButton;
import net.voxelindustry.brokkgui.element.input.GuiCheckbox;
import net.voxelindustry.brokkgui.element.input.GuiRadioButton;
import net.voxelindustry.brokkgui.element.input.GuiTextField;
import net.voxelindustry.brokkgui.element.input.GuiToggleButton;
import net.voxelindustry.brokkgui.markup.attributes.BooleanFormFieldAttributes;
import net.voxelindustry.brokkgui.markup.attributes.ButtonAttributes;
import net.voxelindustry.brokkgui.markup.attributes.GuiElementAttributes;
import net.voxelindustry.brokkgui.markup.attributes.LabelIconAttributes;
import net.voxelindustry.brokkgui.markup.attributes.LinkAttributes;
import net.voxelindustry.brokkgui.markup.attributes.ScrollableAttributes;
import net.voxelindustry.brokkgui.markup.attributes.StyleComponentAttributes;
import net.voxelindustry.brokkgui.markup.attributes.TextComponentAttributes;
import net.voxelindustry.brokkgui.markup.attributes.TextInputAttributes;
import net.voxelindustry.brokkgui.markup.attributes.TextLayoutAttributes;
import net.voxelindustry.brokkgui.markup.attributes.TransformAttributes;
import net.voxelindustry.brokkgui.shape.Circle;
import net.voxelindustry.brokkgui.shape.Line;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.shape.Text;
import net.voxelindustry.brokkgui.text.TextComponent;

public class ElementMarkupSetup
{
    public static void setup()
    {
        MarkupEngine.registerElementDefinition("rectangle",
                new MarkupElementDefinition<>(Rectangle::new)
                        // GuiElement
                        .attributes(TransformAttributes.getAttributes())
                        .attributes(StyleComponentAttributes.getAttributes())
                        .attributes(GuiElementAttributes.getAttributes())
                        .attributes(ScrollableAttributes.getAttributes())
                        .childrenAttributes(TransformAttributes.getChildrenAttributes())
                        .childrenAttributes(StyleComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(GuiElementAttributes.getChildrenAttributes())
                        .childrenAttributes(ScrollableAttributes.getChildrenAttributes())
                        .onAttributesAdded(ScrollableAttributes.getAttributesNames(), ScrollableAttributes.onAttributeAdded())
        );

        MarkupEngine.registerElementDefinition("circle",
                new MarkupElementDefinition<>(Circle::new)
                        // GuiElement
                        .attributes(TransformAttributes.getAttributes())
                        .attributes(StyleComponentAttributes.getAttributes())
                        .attributes(GuiElementAttributes.getAttributes())
                        .attributes(ScrollableAttributes.getAttributes())
                        .childrenAttributes(TransformAttributes.getChildrenAttributes())
                        .childrenAttributes(StyleComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(GuiElementAttributes.getChildrenAttributes())
                        .childrenAttributes(ScrollableAttributes.getChildrenAttributes())
                        .onAttributesAdded(ScrollableAttributes.getAttributesNames(), ScrollableAttributes.onAttributeAdded())
        );

        MarkupEngine.registerElementDefinition("line",
                new MarkupElementDefinition<>(Line::new)
                        // GuiElement
                        .attributes(TransformAttributes.getAttributes())
                        .attributes(StyleComponentAttributes.getAttributes())
                        .attributes(GuiElementAttributes.getAttributes())
                        .attributes(ScrollableAttributes.getAttributes())
                        .childrenAttributes(TransformAttributes.getChildrenAttributes())
                        .childrenAttributes(StyleComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(GuiElementAttributes.getChildrenAttributes())
                        .childrenAttributes(ScrollableAttributes.getChildrenAttributes())
                        .onAttributesAdded(ScrollableAttributes.getAttributesNames(), ScrollableAttributes.onAttributeAdded())
        );

        MarkupEngine.registerElementDefinition("text",
                new MarkupElementDefinition<>(Text::new)
                        // GuiElement
                        .attributes(TransformAttributes.getAttributes())
                        .attributes(StyleComponentAttributes.getAttributes())
                        .attributes(GuiElementAttributes.getAttributes())
                        .attributes(ScrollableAttributes.getAttributes())
                        .childrenAttributes(TransformAttributes.getChildrenAttributes())
                        .childrenAttributes(StyleComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(GuiElementAttributes.getChildrenAttributes())
                        .childrenAttributes(ScrollableAttributes.getChildrenAttributes())
                        .onAttributesAdded(ScrollableAttributes.getAttributesNames(), ScrollableAttributes.onAttributeAdded())
                        // Label
                        .attributes(TextComponentAttributes.getAttributes())
                        .childrenAttributes(TextComponentAttributes.getChildrenAttributes())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
        );

        MarkupEngine.registerElementDefinition("label",
                new MarkupElementDefinition<>(GuiLabel::new)
                        // GuiElement
                        .attributes(TransformAttributes.getAttributes())
                        .attributes(StyleComponentAttributes.getAttributes())
                        .attributes(GuiElementAttributes.getAttributes())
                        .attributes(ScrollableAttributes.getAttributes())
                        .childrenAttributes(TransformAttributes.getChildrenAttributes())
                        .childrenAttributes(StyleComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(GuiElementAttributes.getChildrenAttributes())
                        .childrenAttributes(ScrollableAttributes.getChildrenAttributes())
                        .onAttributesAdded(ScrollableAttributes.getAttributesNames(), ScrollableAttributes.onAttributeAdded())
                        // Label
                        .attributes(TextComponentAttributes.getAttributes())
                        .attributes(TextLayoutAttributes.getAttributes())
                        .attributes(LabelIconAttributes.getAttributes())
                        .childrenAttributes(TextComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(TextLayoutAttributes.getChildrenAttributes())
                        .childrenAttributes(LabelIconAttributes.getChildrenAttributes())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
        );

        MarkupEngine.registerElementDefinition("link",
                new MarkupElementDefinition<>(GuiLink::new)
                        // GuiElement
                        .attributes(TransformAttributes.getAttributes())
                        .attributes(StyleComponentAttributes.getAttributes())
                        .attributes(GuiElementAttributes.getAttributes())
                        .attributes(ScrollableAttributes.getAttributes())
                        .childrenAttributes(TransformAttributes.getChildrenAttributes())
                        .childrenAttributes(StyleComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(GuiElementAttributes.getChildrenAttributes())
                        .childrenAttributes(ScrollableAttributes.getChildrenAttributes())
                        .onAttributesAdded(ScrollableAttributes.getAttributesNames(), ScrollableAttributes.onAttributeAdded())
                        // Label
                        .attributes(TextComponentAttributes.getAttributes())
                        .attributes(TextLayoutAttributes.getAttributes())
                        .attributes(LabelIconAttributes.getAttributes())
                        .childrenAttributes(TextComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(TextLayoutAttributes.getChildrenAttributes())
                        .childrenAttributes(LabelIconAttributes.getChildrenAttributes())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Link
                        .attributes(LinkAttributes.getAttributes())
                        .childrenAttributes(LinkAttributes.getChildrenAttributes())
        );

        MarkupEngine.registerElementDefinition("button",
                new MarkupElementDefinition<>(GuiButton::new)
                        // GuiElement
                        .attributes(TransformAttributes.getAttributes())
                        .attributes(StyleComponentAttributes.getAttributes())
                        .attributes(GuiElementAttributes.getAttributes())
                        .attributes(ScrollableAttributes.getAttributes())
                        .childrenAttributes(TransformAttributes.getChildrenAttributes())
                        .childrenAttributes(StyleComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(GuiElementAttributes.getChildrenAttributes())
                        .childrenAttributes(ScrollableAttributes.getChildrenAttributes())
                        .onAttributesAdded(ScrollableAttributes.getAttributesNames(), ScrollableAttributes.onAttributeAdded())
                        // Label
                        .attributes(TextComponentAttributes.getAttributes())
                        .attributes(TextLayoutAttributes.getAttributes())
                        .attributes(LabelIconAttributes.getAttributes())
                        .childrenAttributes(TextComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(TextLayoutAttributes.getChildrenAttributes())
                        .childrenAttributes(LabelIconAttributes.getChildrenAttributes())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Button
                        .attributes(ButtonAttributes.getAttributes())
                        .childrenAttributes(ButtonAttributes.getChildrenAttributes())
        );

        MarkupEngine.registerElementDefinition("toggle-button",
                new MarkupElementDefinition<>(GuiToggleButton::new)
                        // GuiElement
                        .attributes(TransformAttributes.getAttributes())
                        .attributes(StyleComponentAttributes.getAttributes())
                        .attributes(GuiElementAttributes.getAttributes())
                        .attributes(ScrollableAttributes.getAttributes())
                        .childrenAttributes(TransformAttributes.getChildrenAttributes())
                        .childrenAttributes(StyleComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(GuiElementAttributes.getChildrenAttributes())
                        .childrenAttributes(ScrollableAttributes.getChildrenAttributes())
                        .onAttributesAdded(ScrollableAttributes.getAttributesNames(), ScrollableAttributes.onAttributeAdded())
                        // Label
                        .attributes(TextComponentAttributes.getAttributes())
                        .attributes(TextLayoutAttributes.getAttributes())
                        .attributes(LabelIconAttributes.getAttributes())
                        .childrenAttributes(TextComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(TextLayoutAttributes.getChildrenAttributes())
                        .childrenAttributes(LabelIconAttributes.getChildrenAttributes())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Button
                        .attributes(ButtonAttributes.getAttributes())
                        .childrenAttributes(ButtonAttributes.getChildrenAttributes())
        );

        MarkupEngine.registerElementDefinition("radio-button",
                new MarkupElementDefinition<>(GuiRadioButton::new)
                        // GuiElement
                        .attributes(TransformAttributes.getAttributes())
                        .attributes(StyleComponentAttributes.getAttributes())
                        .attributes(GuiElementAttributes.getAttributes())
                        .attributes(ScrollableAttributes.getAttributes())
                        .childrenAttributes(TransformAttributes.getChildrenAttributes())
                        .childrenAttributes(StyleComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(GuiElementAttributes.getChildrenAttributes())
                        .childrenAttributes(ScrollableAttributes.getChildrenAttributes())
                        .onAttributesAdded(ScrollableAttributes.getAttributesNames(), ScrollableAttributes.onAttributeAdded())
                        // Label
                        .attributes(TextComponentAttributes.getAttributes())
                        .attributes(TextLayoutAttributes.getAttributes())
                        .attributes(LabelIconAttributes.getAttributes())
                        .childrenAttributes(TextComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(TextLayoutAttributes.getChildrenAttributes())
                        .childrenAttributes(LabelIconAttributes.getChildrenAttributes())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Button
                        .attributes(ButtonAttributes.getAttributes())
                        .attributes(BooleanFormFieldAttributes.getAttributes())
                        .childrenAttributes(ButtonAttributes.getChildrenAttributes())
                        .childrenAttributes(BooleanFormFieldAttributes.getChildrenAttributes())
        );

        MarkupEngine.registerElementDefinition("checkbox",
                new MarkupElementDefinition<>(GuiCheckbox::new)
                        // GuiElement
                        .attributes(TransformAttributes.getAttributes())
                        .attributes(StyleComponentAttributes.getAttributes())
                        .attributes(GuiElementAttributes.getAttributes())
                        .attributes(ScrollableAttributes.getAttributes())
                        .childrenAttributes(TransformAttributes.getChildrenAttributes())
                        .childrenAttributes(StyleComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(GuiElementAttributes.getChildrenAttributes())
                        .childrenAttributes(ScrollableAttributes.getChildrenAttributes())
                        .onAttributesAdded(ScrollableAttributes.getAttributesNames(), ScrollableAttributes.onAttributeAdded())
                        // Label
                        .attributes(TextComponentAttributes.getAttributes())
                        .attributes(TextLayoutAttributes.getAttributes())
                        .attributes(LabelIconAttributes.getAttributes())
                        .childrenAttributes(TextComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(TextLayoutAttributes.getChildrenAttributes())
                        .childrenAttributes(LabelIconAttributes.getChildrenAttributes())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Button
                        .attributes(ButtonAttributes.getAttributes())
                        .attributes(BooleanFormFieldAttributes.getAttributes())
                        .childrenAttributes(ButtonAttributes.getChildrenAttributes())
                        .childrenAttributes(BooleanFormFieldAttributes.getChildrenAttributes())
        );

        MarkupEngine.registerElementDefinition("text-field",
                new MarkupElementDefinition<>(GuiTextField::new)
                        // GuiElement
                        .attributes(TransformAttributes.getAttributes())
                        .attributes(StyleComponentAttributes.getAttributes())
                        .attributes(GuiElementAttributes.getAttributes())
                        .attributes(ScrollableAttributes.getAttributes())
                        .childrenAttributes(TransformAttributes.getChildrenAttributes())
                        .childrenAttributes(StyleComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(GuiElementAttributes.getChildrenAttributes())
                        .childrenAttributes(ScrollableAttributes.getChildrenAttributes())
                        .onAttributesAdded(ScrollableAttributes.getAttributesNames(), ScrollableAttributes.onAttributeAdded())
                        // Label
                        .attributes(TextComponentAttributes.getAttributes())
                        .attributes(TextLayoutAttributes.getAttributes())
                        .childrenAttributes(TextComponentAttributes.getChildrenAttributes())
                        .childrenAttributes(TextLayoutAttributes.getChildrenAttributes())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Text field
                        .attributes(TextInputAttributes.getAttributes())
                        .childrenAttributes(TextInputAttributes.getChildrenAttributes())
        );
    }
}
