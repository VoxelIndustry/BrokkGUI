package net.voxelindustry.brokkgui.style;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class ConditionalProperty
{
    private Pattern               pattern;
    private boolean               active;
    private Consumer<StyleHolder> propertyCreator;

    public ConditionalProperty(Pattern pattern, Consumer<StyleHolder> propertyCreator)
    {
        this.pattern = pattern;
        this.active = false;
        this.propertyCreator = propertyCreator;
    }

    public Pattern pattern()
    {
        return pattern;
    }

    public boolean active()
    {
        return active;
    }

    public Consumer<StyleHolder> propertyCreator()
    {
        return propertyCreator;
    }

    public void active(boolean active)
    {
        this.active = active;
    }

    @Override
    public String toString()
    {
        return "ConditionalProperty{" +
                "pattern=" + pattern +
                ", active=" + active +
                ", propertyCreator=" + propertyCreator +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionalProperty that = (ConditionalProperty) o;
        return active == that.active &&
                Objects.equals(pattern, that.pattern) &&
                Objects.equals(propertyCreator, that.propertyCreator);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(pattern, active, propertyCreator);
    }
}
