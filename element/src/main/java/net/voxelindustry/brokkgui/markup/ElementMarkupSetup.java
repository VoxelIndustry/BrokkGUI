package net.voxelindustry.brokkgui.markup;

import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.element.GuiLink;
import net.voxelindustry.brokkgui.element.input.GuiButton;
import net.voxelindustry.brokkgui.element.input.GuiCheckbox;
import net.voxelindustry.brokkgui.element.input.GuiRadioButton;
import net.voxelindustry.brokkgui.element.input.GuiTextField;
import net.voxelindustry.brokkgui.element.input.GuiToggleButton;
import net.voxelindustry.brokkgui.element.menu.GuiSelect;
import net.voxelindustry.brokkgui.element.pane.GuiPane;
import net.voxelindustry.brokkgui.element.pane.ScrollPane;
import net.voxelindustry.brokkgui.markup.attributes.BooleanFormFieldAttributes;
import net.voxelindustry.brokkgui.markup.attributes.ButtonAttributes;
import net.voxelindustry.brokkgui.markup.attributes.GuiElementAttributes;
import net.voxelindustry.brokkgui.markup.attributes.LabelIconAttributes;
import net.voxelindustry.brokkgui.markup.attributes.LinkAttributes;
import net.voxelindustry.brokkgui.markup.attributes.MenuSelectAttributes;
import net.voxelindustry.brokkgui.markup.attributes.ScrollableAttributes;
import net.voxelindustry.brokkgui.markup.attributes.StyleComponentAttributes;
import net.voxelindustry.brokkgui.markup.attributes.TextAssistAttributes;
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
        MarkupEngine.registerElementDefinition("pane",
                new MarkupElementDefinition<>(GuiPane::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
        );

        MarkupEngine.registerElementDefinition("scrollpane",
                new MarkupElementDefinition<>(ScrollPane::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
        );

        MarkupEngine.registerElementDefinition("rectangle",
                new MarkupElementDefinition<>(Rectangle::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
        );

        MarkupEngine.registerElementDefinition("circle",
                new MarkupElementDefinition<>(Circle::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
        );

        MarkupEngine.registerElementDefinition("line",
                new MarkupElementDefinition<>(Line::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
        );

        MarkupEngine.registerElementDefinition("text",
                new MarkupElementDefinition<>(Text::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
                        // Label
                        .attributeGroup(TextComponentAttributes.instance())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
        );

        MarkupEngine.registerElementDefinition("label",
                new MarkupElementDefinition<>(GuiLabel::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
                        // Label
                        .attributeGroup(TextComponentAttributes.instance())
                        .attributeGroup(TextLayoutAttributes.instance())
                        .attributeGroup(LabelIconAttributes.instance())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
        );

        MarkupEngine.registerElementDefinition("link",
                new MarkupElementDefinition<>(GuiLink::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
                        // Label
                        .attributeGroup(TextComponentAttributes.instance())
                        .attributeGroup(TextLayoutAttributes.instance())
                        .attributeGroup(LabelIconAttributes.instance())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Link
                        .attributeGroup(LinkAttributes.instance())
        );

        MarkupEngine.registerElementDefinition("button",
                new MarkupElementDefinition<>(GuiButton::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
                        // Label
                        .attributeGroup(TextComponentAttributes.instance())
                        .attributeGroup(TextLayoutAttributes.instance())
                        .attributeGroup(LabelIconAttributes.instance())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Button
                        .attributeGroup(ButtonAttributes.instance())
        );

        MarkupEngine.registerElementDefinition("toggle-button",
                new MarkupElementDefinition<>(GuiToggleButton::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
                        // Label
                        .attributeGroup(TextComponentAttributes.instance())
                        .attributeGroup(TextLayoutAttributes.instance())
                        .attributeGroup(LabelIconAttributes.instance())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Button
                        .attributeGroup(ButtonAttributes.instance())
        );

        MarkupEngine.registerElementDefinition("radio-button",
                new MarkupElementDefinition<>(GuiRadioButton::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
                        // Label
                        .attributeGroup(TextComponentAttributes.instance())
                        .attributeGroup(TextLayoutAttributes.instance())
                        .attributeGroup(LabelIconAttributes.instance())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Button
                        .attributeGroup(ButtonAttributes.instance())
                        .attributeGroup(BooleanFormFieldAttributes.instance())
        );

        MarkupEngine.registerElementDefinition("checkbox",
                new MarkupElementDefinition<>(GuiCheckbox::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
                        // Label
                        .attributeGroup(TextComponentAttributes.instance())
                        .attributeGroup(TextLayoutAttributes.instance())
                        .attributeGroup(LabelIconAttributes.instance())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Button
                        .attributeGroup(ButtonAttributes.instance())
                        .attributeGroup(BooleanFormFieldAttributes.instance())
        );

        MarkupEngine.registerElementDefinition("text-field",
                new MarkupElementDefinition<>(GuiTextField::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
                        // Label
                        .attributeGroup(TextComponentAttributes.instance())
                        .attributeGroup(TextLayoutAttributes.instance())
                        .attributeGroup(LabelIconAttributes.instance())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Text field
                        .attributeGroup(TextInputAttributes.instance())
                        .attributeGroup(TextAssistAttributes.instance())
        );

        MarkupEngine.registerElementDefinition("select",
                new MarkupElementDefinition<>(GuiSelect::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
                        // Label
                        .attributeGroup(TextComponentAttributes.instance())
                        .attributeGroup(TextLayoutAttributes.instance())
                        // Text creator
                        .textChildReceiver((attribute, element) -> element.get(TextComponent.class).text(attribute))
                        // Button
                        .attributeGroup(ButtonAttributes.instance())
                        // Menu
                        .attributeGroup(MenuSelectAttributes.instance())
        );
    }
}
