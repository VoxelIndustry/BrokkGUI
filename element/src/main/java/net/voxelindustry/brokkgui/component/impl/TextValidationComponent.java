package net.voxelindustry.brokkgui.component.impl;

import fr.ourten.teabeans.property.ListProperty;
import fr.ourten.teabeans.property.Property;
import net.voxelindustry.brokkgui.component.GuiComponent;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.text.TextComponent;
import net.voxelindustry.brokkgui.validation.BaseTextValidator;

import java.util.List;

public class TextValidationComponent extends GuiComponent
{
    private final Property<Boolean> validProperty = new Property<>(true);

    private final ListProperty<BaseTextValidator> validatorsProperty = new ListProperty<>(null);

    private TextComponent textComponent;

    @Override
    public void attach(GuiElement element)
    {
        super.attach(element);

        textComponent = element.get(TextComponent.class);
        textComponent.textProperty().addListener(((observable, oldValue, newValue) -> validate()));
    }

    public Property<Boolean> validProperty()
    {
        return validProperty;
    }

    public boolean valid()
    {
        return validProperty().getValue();
    }

    public void valid(boolean valid)
    {
        validProperty().setValue(valid);
    }

    public ListProperty<BaseTextValidator> validatorsProperty()
    {
        return validatorsProperty;
    }

    public List<BaseTextValidator> validators()
    {
        return validatorsProperty().getValue();
    }

    public void addValidator(BaseTextValidator validator)
    {
        validatorsProperty().add(validator);
    }

    public void removeValidator(BaseTextValidator validator)
    {
        validatorsProperty().remove(validator);
    }

    public void validate()
    {
        valid(true);
        if (!validators().isEmpty())
            validators().forEach(validator ->
            {
                validator.setErrored(false);
                validator.validate(textComponent.text());
                if (validator.isErrored())
                    valid(false);
            });
    }
}
