package net.voxelindustry.brokkgui.style;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import net.voxelindustry.brokkgui.style.parser.StylesheetParser;
import net.voxelindustry.brokkgui.style.tree.StyleList;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class StylesheetManager
{
    private static StylesheetManager INSTANCE;

    public static StylesheetManager getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new StylesheetManager();
        return INSTANCE;
    }

    private Logger logger;

    private LoadingCache<String, StyleList> styleCache;
    private StylesheetParser                styleParser;

    public final String             DEFAULT_THEME = "BROKKGUI";
    private      List<String>       themeIDs;
    private      List<List<String>> styleSheets;
    private      List<StyleList>    userAgents;

    private StylesheetManager()
    {
        logger = Logger.getLogger("BrokkGui CSS Loader");

        styleCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, StyleList>()
                {
                    @Override
                    public StyleList load(@Nonnull String stylesheet) throws IOException
                    {
                        return styleParser.loadStylesheet(stylesheet);
                    }

                    @Override
                    public ListenableFuture<StyleList> reload(String key, StyleList oldValue) throws Exception
                    {
                        logger.info("Stylesheet has been forced to reload. path=" + key);
                        return super.reload(key, oldValue);
                    }
                });
        styleParser = new StylesheetParser(logger);

        themeIDs = new ArrayList<>();
        styleSheets = new ArrayList<>();
        userAgents = new ArrayList<>();
    }

    public void forceReload(IStyleRoot screen)
    {
        forceReload(screen, true);
    }

    public void forceReload(IStyleRoot screen, boolean useUserAgent)
    {
        screen.getStylesheets().forEach(styleCache::refresh);
        refreshStylesheets(screen, useUserAgent);

    }

    public void refreshStylesheets(IStyleRoot screen)
    {
        refreshStylesheets(screen, true);
    }

    public void refreshStylesheets(IStyleRoot screen, boolean useUserAgent)
    {
        StyleList list;

        if (useUserAgent)
            list = new StyleList(getUserAgent(screen.getThemeID()));
        else
            list = new StyleList();

        try
        {
            list.merge(loadStylesheets(screen.getStylesheets().toArray(new String[0])));
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        screen.setStyleList(list);
    }

    StyleList loadStylesheets(String... styleSheets) throws ExecutionException
    {
        StyleList list = new StyleList();

        for (String styleSheet : styleSheets)
            list.merge(getStyleList(styleSheet));
        return list;
    }

    StyleList getStyleList(String styleSheet) throws ExecutionException
    {
        return styleCache.get(styleSheet);
    }

    public StyleList loadDependencies(String styleSheet, List<String> dependencies)
    {
        try
        {
            return loadStylesheets(dependencies.toArray(new String[0]));
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    ////////////////
    // USER-AGENT //
    ////////////////

    public void addUserAgent(String themeID, String styleSheet)
    {
        if (StringUtils.isEmpty(themeID) || DEFAULT_THEME.equals(themeID))
            throw new IllegalArgumentException("Invalid themeID " + themeID);
        try
        {
            if (!themeIDs.contains(themeID))
                createUserAgent(themeID);

            int index = themeIDs.indexOf(themeID);

            if (styleSheets.get(index).contains(styleSheet))
                return;

            styleSheets.get(index).add(styleSheet);
            userAgents.get(index).merge(getStyleList(styleSheet));

        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }

    public void removeUserAgent(String themeID, String styleSheet)
    {
        if (StringUtils.isEmpty(themeID) || DEFAULT_THEME.equals(themeID))
            throw new IllegalArgumentException("Invalid themeID " + themeID);
        if (!themeIDs.contains(themeID))
            return;

        int index = themeIDs.indexOf(themeID);

        if (!styleSheets.get(index).contains(styleSheet))
            return;

        styleSheets.get(index).remove(styleSheet);
        try
        {
            userAgents.set(index, loadStylesheets(styleSheets.get(index).toArray(
                    new String[styleSheets.get(index).size()])));
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }

    private StyleList getUserAgent(String themeID)
    {
        if (!themeIDs.contains(themeID))
            createUserAgent(themeID);
        return userAgents.get(themeIDs.indexOf(themeID));
    }

    private void createUserAgent(String themeID)
    {
        try
        {
            themeIDs.add(themeID);
            styleSheets.add(new ArrayList<>());
            userAgents.add(new StyleList());

            styleSheets.get(themeIDs.indexOf(themeID)).add("/assets/brokkgui/css/user_agent.css");

            userAgents.get(themeIDs.indexOf(themeID)).merge(
                    getStyleList("/assets/brokkgui/css/user_agent.css"));
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }
}
