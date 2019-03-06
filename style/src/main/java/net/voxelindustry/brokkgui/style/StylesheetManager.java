package net.voxelindustry.brokkgui.style;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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

        this.styleCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build(new CacheLoader<String, StyleList>()
                {
                    @Override
                    public StyleList load(@Nonnull String stylesheet) throws IOException
                    {
                        return styleParser.loadStylesheet(stylesheet);
                    }
                });
        this.styleParser = new StylesheetParser(logger);

        this.themeIDs = new ArrayList<>();
        this.styleSheets = new ArrayList<>();
        this.userAgents = new ArrayList<>();
    }

    public void refreshStylesheets(IStyleRoot screen)
    {
        StyleList list = new StyleList(this.getUserAgent(screen.getThemeID()));

        try
        {
            list.merge(this.loadStylesheets(screen.getStylesheetsProperty().getValue().toArray(
                    new String[screen.getStylesheetsProperty().size()])));
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        screen.setStyleTree(list);
    }

    StyleList loadStylesheets(String... styleSheets) throws ExecutionException
    {
        StyleList list = new StyleList();

        for (String styleSheet : styleSheets)
            list.merge(this.getStyleList(styleSheet));
        return list;
    }

    StyleList getStyleList(String styleSheet) throws ExecutionException
    {
        return this.styleCache.get(styleSheet);
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
        if (StringUtils.isEmpty(themeID) || this.DEFAULT_THEME.equals(themeID))
            throw new IllegalArgumentException("Invalid themeID " + themeID);
        try
        {
            if (!this.themeIDs.contains(themeID))
                this.createUserAgent(themeID);

            int index = this.themeIDs.indexOf(themeID);

            if (this.styleSheets.get(index).contains(styleSheet))
                return;

            this.styleSheets.get(index).add(styleSheet);
            this.userAgents.get(index).merge(this.getStyleList(styleSheet));

        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }

    public void removeUserAgent(String themeID, String styleSheet)
    {
        if (StringUtils.isEmpty(themeID) || this.DEFAULT_THEME.equals(themeID))
            throw new IllegalArgumentException("Invalid themeID " + themeID);
        if (!this.themeIDs.contains(themeID))
            return;

        int index = this.themeIDs.indexOf(themeID);

        if (!this.styleSheets.get(index).contains(styleSheet))
            return;

        this.styleSheets.get(index).remove(styleSheet);
        try
        {
            this.userAgents.set(index, this.loadStylesheets(this.styleSheets.get(index).toArray(
                    new String[this.styleSheets.get(index).size()])));
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }

    private StyleList getUserAgent(String themeID)
    {
        if (!this.themeIDs.contains(themeID))
            this.createUserAgent(themeID);
        return this.userAgents.get(this.themeIDs.indexOf(themeID));
    }

    private void createUserAgent(String themeID)
    {
        try
        {
            this.themeIDs.add(themeID);
            this.styleSheets.add(new ArrayList<>());
            this.userAgents.add(new StyleList());

            this.styleSheets.get(this.themeIDs.indexOf(themeID)).add("/assets/brokkgui/css/user_agent.css");

            this.userAgents.get(this.themeIDs.indexOf(themeID)).merge(
                    this.getStyleList("/assets/brokkgui/css/user_agent.css"));
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }
}
