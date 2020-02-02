package net.voxelindustry.brokkgui.sprite;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.voxelindustry.brokkgui.animation.Interpolator;
import net.voxelindustry.brokkgui.animation.Interpolators;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class SpriteAnimationParser extends TypeAdapter<SpriteAnimation>
{
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(SpriteAnimation.class, new SpriteAnimationParser()).create();

    private static final LoadingCache<String, SpriteAnimation> ANIMATION_CACHE = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(new CacheLoader<String, SpriteAnimation>()
            {
                @Override
                public SpriteAnimation load(@Nonnull String resource) throws IOException
                {
                    return readAnimation(resource);
                }
            });

    private static SpriteAnimation readAnimation(String resource) throws IOException
    {
        String json = IOUtils.toString(SpriteAnimationParser.class.getResourceAsStream(resource), StandardCharsets.UTF_8);
        return parse(json);
    }

    public static SpriteAnimation getAnimation(String resource)
    {
        try
        {
            return ANIMATION_CACHE.get(resource);
        } catch (Exception e)
        {
            System.err.println("Unable to load SpriteAnimation. resource=" + resource);
            e.printStackTrace();
        }

        return null;
    }

    static SpriteAnimation parse(String json)
    {
        return GSON.fromJson(json, SpriteAnimation.class);
    }

    @Override
    public void write(JsonWriter out, SpriteAnimation value)
    {
        throw new UnsupportedOperationException("SpriteAnimation cannot be turned back into a Json.");
    }

    @Override
    public SpriteAnimation read(JsonReader in) throws IOException
    {
        int frameCount = 0;
        long duration = 0;
        boolean isVertical = false;
        Interpolator interpolator = Interpolators.LINEAR;
        Map<Integer, Long> parsedFrameTimes = new HashMap<>();

        in.beginObject();
        while (in.hasNext())
        {
            String key = in.nextName();
            switch (key)
            {
                case "frameCount":
                    frameCount = in.nextInt();
                    break;
                case "duration":
                    duration = in.nextLong();
                    break;
                case "orientation":
                    if (in.nextString().equalsIgnoreCase("VERTICAL"))
                    {
                        isVertical = true;
                    }
                    break;
                case "interpolator":
                    String interpolatorName = in.nextString();
                    try
                    {
                        interpolator = Interpolators.fromName(interpolatorName);
                    } catch (RuntimeException e)
                    {
                        throw new JsonParseException("Cannot parse SpriteAnimation JSON. Unknown interpolator [" + interpolatorName + "]");
                    }
                    break;
                case "frames":
                    in.beginArray();

                    while (in.hasNext())
                    {
                        int index = 0;
                        long time = 0;

                        in.beginObject();
                        while (in.hasNext())
                        {
                            switch (in.nextName())
                            {
                                case "index":
                                    index = in.nextInt();
                                    break;
                                case "time":
                                    time = in.nextLong();
                                    break;
                            }
                        }
                        in.endObject();

                        parsedFrameTimes.put(index, time);
                    }
                    in.endArray();
                    break;
            }
        }
        in.endObject();

        long finalDuration = duration;
        int finalFrameCount = frameCount;
        Interpolator finalInterpolator = interpolator;
        Map<Integer, Long> framesTimeByIndex = IntStream.range(0, frameCount)
                .boxed()
                .collect(toMap(Function.identity(),
                        index ->
                        {
                            long previousValue = (long) (finalInterpolator.apply((float) (index) / finalFrameCount) * finalDuration);
                            long nextValue = (long) (finalInterpolator.apply((float) (index + 1) / finalFrameCount) * finalDuration);

                            return parsedFrameTimes.getOrDefault(index, nextValue - previousValue);
                        }));

        Map<Integer, Long> summedFramesTimeByIndex = framesTimeByIndex.keySet().stream()
                .map(index -> Pair.of(index, IntStream.rangeClosed(0, index).mapToLong(framesTimeByIndex::get).sum()))
                .collect(toMap(Pair::getKey, Pair::getValue));

        return new SpriteAnimation(
                isVertical,
                frameCount,
                summedFramesTimeByIndex);
    }
}
