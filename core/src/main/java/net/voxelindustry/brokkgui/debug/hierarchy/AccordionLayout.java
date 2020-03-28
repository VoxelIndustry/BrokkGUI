package net.voxelindustry.brokkgui.debug.hierarchy;

import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.voxelindustry.brokkgui.util.MathUtils.clamp;

public class AccordionLayout
{
    public static void openItem(AccordionItem item, List<AccordionItem> items, float maxSize)
    {
        int index = items.indexOf(item);

        if (index == 0)
        {
            moveDown(item.getMinimalSize(), maxSize, items.get(index + 1), items);
        }
        else if (index == items.size() - 1)
        {
            moveUp(item.getMinimalSize(), item, items);
        }
        else
        {
            float movedDown = moveDown(item.getMinimalSize(), maxSize, items.get(index + 1), items);

            if (movedDown < item.getMinimalSize())
                moveUp(item.getMinimalSize() - movedDown, item, items);
        }
    }

    public static void closeItem(AccordionItem item, List<AccordionItem> items, float maxSize)
    {
        int index = items.indexOf(item);

        if (isLast(item, items))
        {
            moveDown(maxSize - item.getHeaderPos() - item.getHeaderSize(), maxSize, item, items);

            AccordionItem lastMoved = item;
            while (index != 1)
            {
                AccordionItem prevAdjacent = items.get(index - 1);

                if (!prevAdjacent.isCollapsed())
                    break;

                moveDown(lastMoved.getHeaderPos() - prevAdjacent.getHeaderSize(), maxSize, prevAdjacent, items);

                lastMoved = prevAdjacent;
                index--;
            }
            return;
        }

        AccordionItem nextAdjacent = items.get(index + 1);

        if (isLast(getLastCollapsedDown(item, items), items))
        {
            moveDown(item.getHeaderPos() - nextAdjacent.getHeaderPos() + item.getHeaderSize(), maxSize, item, items);
            return;
        }

        float toMoveUp = nextAdjacent.getHeaderPos() - item.getHeaderPos() - item.getHeaderSize();

        for (AccordionItem next : items.subList(index + 1, items.size()))
            moveUp(toMoveUp, next, items);
    }

    public static float moveUp(float amount, AccordionItem item, List<AccordionItem> items)
    {
        if (items.indexOf(item) == 0)
            return 0;

        AccordionItem previousItem = items.get(items.indexOf(item) - 1);

        float availableSpace = max(0,
                item.getHeaderPos() - previousItem.getHeaderPos() - getItemHeight(previousItem));

        if (availableSpace < amount)
        {
            float upwardMoved = availableSpace + moveUp(amount - availableSpace, previousItem, items);
            item.setHeaderPos(item.getHeaderPos() - upwardMoved);
            return upwardMoved;
        }
        item.setHeaderPos(item.getHeaderPos() - min(amount, availableSpace));
        return min(amount, availableSpace);
    }

    public static float moveDown(float amount, float maxSize, AccordionItem item, List<AccordionItem> items)
    {
        if (items.indexOf(item) == items.size() - 1)
        {
            float moveDown = clamp(
                    0,
                    amount,
                    maxSize - item.getHeaderPos() - getItemHeight(item));
            item.setHeaderPos(item.getHeaderPos() + moveDown);
            return moveDown;
        }

        AccordionItem nextItem = items.get(items.indexOf(item) + 1);

        float availableSpace = max(0,
                nextItem.getHeaderPos() - item.getHeaderPos() - getItemHeight(item));

        if (availableSpace < amount)
        {
            float downwardMoved = availableSpace + moveDown(amount - availableSpace, maxSize, nextItem, items);
            item.setHeaderPos(item.getHeaderPos() + downwardMoved);
            return downwardMoved;
        }
        item.setHeaderPos(item.getHeaderPos() - min(amount, availableSpace));
        return min(amount, availableSpace);
    }

    private static float getItemHeight(AccordionItem item)
    {
        if (item.isCollapsed())
            return item.getHeaderSize();
        return item.getHeaderSize() + item.getMinimalSize();
    }

    private static AccordionItem getLastCollapsedDown(AccordionItem item, List<AccordionItem> items)
    {
        int index = items.indexOf(item) + 1;
        AccordionItem next = item;

        while (index < items.size())
        {
            next = items.get(index);
            if (!next.isCollapsed())
                return item;
            index++;
        }
        return next;
    }

    private static AccordionItem getLastCollapsedUp(AccordionItem item, List<AccordionItem> items)
    {
        int index = items.indexOf(item) - 1;
        AccordionItem prev = item;

        while (index > 0)
        {
            prev = items.get(index);
            if (!prev.isCollapsed())
                return item;
            index--;
        }
        return prev;
    }

    private static boolean isLast(AccordionItem item, List<AccordionItem> items)
    {
        return items.indexOf(item) == items.size() - 1;
    }

    private static boolean isFirst(AccordionItem item, List<AccordionItem> items)
    {
        return items.indexOf(item) == 0;
    }
}
