package fr.ourten.brokkgui.event;

public class EventType<T extends BrokkGuiEvent>
{
    public static final EventType<BrokkGuiEvent> ROOT = new EventType<>("ROOT", null);

    private final String                         name;
    private final EventType<? super T>           parent;

    public EventType(final EventType<? super T> parent, final String name)
    {
        if (parent == null)
            throw new IllegalArgumentException("EventType parent cannot be null!");
        if (parent == this)
            throw new IllegalArgumentException("EventType parent cannot be itself!");
        this.name = name;
        this.parent = parent;
    }

    public EventType(final String name)
    {
        this(EventType.ROOT, name);
    }

    EventType(final String name, final EventType<? super T> parent)
    {
        this.name = name;
        this.parent = parent;
    }

    public String getName()
    {
        return this.name;
    }

    public EventType<? super T> getParent()
    {
        return this.parent;
    }

    public boolean isSubType(final EventType<?> eventType)
    {
        if (eventType == this || eventType == EventType.ROOT || this.getParent() == eventType)
            return true;
        if (this == EventType.ROOT)
            return false;
        return this.getParent().isSubType(eventType);
    }

    @Override
    public String toString()
    {
        return "EventType [name=" + this.name + ", parent=" + this.parent.toString() + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.name == null ? 0 : this.name.hashCode());
        result = prime * result + (this.parent == null ? 0 : this.parent.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final EventType<?> other = (EventType<?>) obj;
        if (this.name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.parent == null)
        {
            if (other.parent != null)
                return false;
        }
        else if (!this.parent.equals(other.parent))
            return false;
        return true;
    }
}