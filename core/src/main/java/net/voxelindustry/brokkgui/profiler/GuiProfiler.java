package net.voxelindustry.brokkgui.profiler;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.math.Quantiles;
import net.voxelindustry.brokkgui.component.GuiElement;
import net.voxelindustry.brokkgui.internal.profiler.IProfiler;
import net.voxelindustry.brokkgui.style.StyleComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static com.google.common.math.Quantiles.median;
import static com.google.common.math.Stats.meanOf;
import static java.lang.String.format;
import static java.lang.System.nanoTime;

public class GuiProfiler implements IProfiler
{
    private final LinkedHashMap<GuiElement, Long> styleRefreshCounters = new LinkedHashMap<>();

    private final LinkedListMultimap<GuiElement, Long> renderTimes       = LinkedListMultimap.create();
    private final LinkedListMultimap<GuiElement, Long> styleRefreshTimes = LinkedListMultimap.create();

    private final List<Long> frameRenderTimes = new ArrayList<>();

    private Map<GuiElement, Long> currentNodeRenderTime       = new HashMap<>();
    private Map<GuiElement, Long> currentNodeStyleRefreshTime = new HashMap<>();

    private long currentFrameTime;

    @Override
    public void preElementRender(GuiElement node)
    {
        currentNodeRenderTime.put(node, nanoTime());
    }

    @Override
    public void postElementRender(GuiElement node)
    {
        renderTimes.put(node, nanoTime() - currentNodeRenderTime.remove(node));

        if (renderTimes.size() > 10_000)
            renderTimes.entries().remove(0);
    }

    @Override
    public void preElementStyleRefresh(GuiElement node)
    {
        styleRefreshCounters.compute(node, (key, value) -> value == null ? 1 : value + 1);
        currentNodeStyleRefreshTime.put(node, nanoTime());
    }

    @Override
    public void postElementStyleRefresh(GuiElement node)
    {
        styleRefreshTimes.put(node, nanoTime() - currentNodeStyleRefreshTime.remove(node));

        if (styleRefreshTimes.size() > 10_000)
            styleRefreshTimes.entries().remove(0);
    }

    @Override
    public void beginRenderFrame()
    {
        currentFrameTime = nanoTime();
    }

    @Override
    public void endRenderFrame()
    {
        frameRenderTimes.add(nanoTime() - currentFrameTime);

        if (frameRenderTimes.size() > 10_000)
            frameRenderTimes.remove(0);
    }

    public String getHumanReport()
    {
        StringJoiner builder = new StringJoiner(System.lineSeparator());

        builder.add("");
        builder.add("===========================");
        builder.add(format("Frame render time: %s (AVG) %s (MED) %s (MIN) %s (MAX)",
                frameRenderTimes.stream().mapToLong(Long::longValue).average().orElse(0) / 1_000_000D + "ms",
                median().compute(frameRenderTimes) / 1_000_000D + "ms",
                frameRenderTimes.stream().mapToLong(Long::longValue).min().orElse(0) / 1_000_000D + "ms",
                frameRenderTimes.stream().mapToLong(Long::longValue).max().orElse(0) / 1_000_000D + "ms"));

        builder.add(format("Unique nodes: %d (rendered) %d (styled)", renderTimes.keySet().size(), styleRefreshTimes.keySet().size()));

        builder.add("Render time top 10 (AVG):");

        Comparator<Pair<GuiElement, Double>> doubleByNodeComparator = Comparator.comparing(Pair::getValue);
        Comparator<Pair<GuiElement, Long>> longByNodeComparator = Comparator.comparing(Pair::getValue);

        renderTimes.asMap().entrySet().stream()
                .map(renderTimesByNode -> Pair.of(renderTimesByNode.getKey(), meanOf(renderTimesByNode.getValue())))
                .sorted(doubleByNodeComparator.reversed())
                .limit(10)
                .forEach(renderTimeByNode -> builder.add(format("- %s = %s", getNodePrettyName(renderTimeByNode.getKey()), renderTimeByNode.getValue() / 1_000_000 + "ms")));

        builder.add("Render time top 10 (MED):");

        renderTimes.asMap().entrySet().stream()
                .map(renderTimesByNode -> Pair.of(renderTimesByNode.getKey(), median().compute(renderTimesByNode.getValue())))
                .sorted(doubleByNodeComparator.reversed())
                .limit(10)
                .forEach(renderTimeByNode -> builder.add(format("- %s = %s", getNodePrettyName(renderTimeByNode.getKey()), renderTimeByNode.getValue() / 1_000_000 + "ms")));

        builder.add("Render time top 10 (MAX):");

        renderTimes.asMap().entrySet().stream()
                .map(renderTimesByNode -> Pair.of(renderTimesByNode.getKey(), renderTimesByNode.getValue().stream().mapToLong(Long::longValue).max().orElse(0)))
                .sorted(longByNodeComparator.reversed())
                .limit(10)
                .forEach(renderTimeByNode -> builder.add(format("- %s = %s", getNodePrettyName(renderTimeByNode.getKey()), renderTimeByNode.getValue() / 1_000_000 + "ms")));

        builder.add("Render time top 10 (MIN):");

        renderTimes.asMap().entrySet().stream()
                .map(renderTimesByNode -> Pair.of(renderTimesByNode.getKey(), renderTimesByNode.getValue().stream().mapToLong(Long::longValue).min().orElse(0)))
                .sorted(longByNodeComparator.reversed())
                .limit(10)
                .forEach(renderTimeByNode -> builder.add(format("- %s = %s", getNodePrettyName(renderTimeByNode.getKey()), renderTimeByNode.getValue() / 1_000_000 + "ms")));

        if (styleRefreshCounters.isEmpty())
            builder.add("Style profiling is not enabled.");
        else
        {
            builder.add("Style refresh top 10:");

            styleRefreshCounters.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .limit(10)
                    .forEach(counterByNode -> builder.add(format("- %s = %d", getNodePrettyName(counterByNode.getKey()), counterByNode.getValue())));
        }

        builder.add("===========================");

        return builder.toString();
    }

    private String getNodePrettyName(GuiElement node)
    {
        StringBuilder name = new StringBuilder();
        if (!StringUtils.isEmpty(node.id()))
            name.append(node.id());
        else
            name.append(node.type()).append(" (").append(node.getClass().getSimpleName()).append(")");

        StyleComponent style = node.get(StyleComponent.class);
        if (!style.styleClass().isEmpty())
        {
            name.append(" [ ");
            style.styleClass().getValue().forEach(styleClass -> name.append(".").append(styleClass).append(" "));
            name.append("]");
        }
        return name.toString();
    }

    public long getRecordsCount()
    {
        return renderTimes.size() + styleRefreshTimes.size() + styleRefreshCounters.size() + frameRenderTimes.size();
    }

    public double getFrameRenderTimePercentile(int percentile)
    {
        return Quantiles.percentiles().index(percentile).compute(frameRenderTimes);
    }

    public int getFrameCount()
    {
        return frameRenderTimes.size();
    }

    public long getFrameRenderTimeMax()
    {
        return frameRenderTimes.stream().mapToLong(Long::longValue).max().orElse(0);
    }
}
