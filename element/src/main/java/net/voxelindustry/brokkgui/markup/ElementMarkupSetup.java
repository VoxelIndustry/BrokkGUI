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
import net.voxelindustry.brokkgui.markup.attributes.LabelIconAttributes;
import net.voxelindustry.brokkgui.markup.attributes.LinkAttributes;
import net.voxelindustry.brokkgui.markup.attributes.MenuDisplayListAttributes;
import net.voxelindustry.brokkgui.markup.attributes.MenuSelectAttributes;
import net.voxelindustry.brokkgui.markup.attributes.ScrollableAttributes;
import net.voxelindustry.brokkgui.markup.attributes.TextAssistAttributes;
import net.voxelindustry.brokkgui.markup.attributes.TextInputAttributes;
import net.voxelindustry.brokkgui.markup.attributes.elements.GuiElementAttributes;
import net.voxelindustry.brokkgui.markup.attributes.elements.HorizontalLayoutAttributes;
import net.voxelindustry.brokkgui.markup.attributes.elements.StyleComponentAttributes;
import net.voxelindustry.brokkgui.markup.attributes.elements.TextComponentAttributes;
import net.voxelindustry.brokkgui.markup.attributes.elements.TextLayoutAttributes;
import net.voxelindustry.brokkgui.markup.attributes.elements.TransformAttributes;
import net.voxelindustry.brokkgui.markup.attributes.elements.VerticalLayoutAttributes;
import net.voxelindustry.brokkgui.markup.definitions.MarkupElementDefinition;
import net.voxelindustry.brokkgui.shape.Circle;
import net.voxelindustry.brokkgui.shape.Line;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.shape.Text;
import net.voxelindustry.brokkgui.text.TextComponent;

public class ElementMarkupSetup
{
    public static void setup()
    {
        MarkupElementRegistry.registerElementDefinition("pane",
                new MarkupElementDefinition<>(GuiPane::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
        );

        MarkupElementRegistry.registerElementDefinition("vertical-pane",
                new MarkupElementDefinition<>(GuiPane::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
                        // Layout
                        .attributeGroup(VerticalLayoutAttributes.instance())
        );

        MarkupElementRegistry.registerElementDefinition("horizontal-pane",
                new MarkupElementDefinition<>(GuiPane::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
                        // Layout
                        .attributeGroup(HorizontalLayoutAttributes.instance())
        );

        MarkupElementRegistry.registerElementDefinition("scrollpane",
                new MarkupElementDefinition<>(ScrollPane::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
        );

        MarkupElementRegistry.registerElementDefinition("rectangle",
                new MarkupElementDefinition<>(Rectangle::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
        );

        MarkupElementRegistry.registerElementDefinition("circle",
                new MarkupElementDefinition<>(Circle::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
        );

        MarkupElementRegistry.registerElementDefinition("line",
                new MarkupElementDefinition<>(Line::new)
                        // GuiElement
                        .attributeGroup(TransformAttributes.instance())
                        .attributeGroup(GuiElementAttributes.instance())
                        .attributeGroup(ScrollableAttributes.instance())
                        // Styling
                        .attributeGroup(StyleComponentAttributes.instance())
        );

        MarkupElementRegistry.registerElementDefinition("text",
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

        MarkupElementRegistry.registerElementDefinition("label",
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

        MarkupElementRegistry.registerElementDefinition("link",
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

        MarkupElementRegistry.registerElementDefinition("button",
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

        MarkupElementRegistry.registerElementDefinition("toggle-button",
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

        MarkupElementRegistry.registerElementDefinition("radio-button",
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

        MarkupElementRegistry.registerElementDefinition("checkbox",
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

        MarkupElementRegistry.registerElementDefinition("text-field",
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

        MarkupElementRegistry.registerElementDefinition("select",
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
                        .attributeGroup(MenuDisplayListAttributes.instance())
        );
    }
}
