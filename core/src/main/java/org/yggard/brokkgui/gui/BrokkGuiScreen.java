package org.yggard.brokkgui.gui;

import fr.ourten.teabeans.binding.BaseExpression;
import fr.ourten.teabeans.value.BaseListProperty;
import fr.ourten.teabeans.value.BaseProperty;
import org.yggard.brokkgui.GuiFocusManager;
import org.yggard.brokkgui.event.WindowEvent;
import org.yggard.brokkgui.internal.IBrokkGuiImpl;
import org.yggard.brokkgui.internal.IGuiRenderer;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.EGuiRenderPass;
import org.yggard.brokkgui.panel.GuiPane;
import org.yggard.brokkgui.style.StylesheetManager;
import org.yggard.brokkgui.style.tree.StyleList;
import org.yggard.hermod.EventDispatcher;
import org.yggard.hermod.EventHandler;

import java.util.ArrayList;
import java.util.Collections;

public class BrokkGuiScreen implements IGuiWindow
{
    private EventDispatcher                 eventDispatcher;
    private EventHandler<WindowEvent.Open>  onOpenEvent;
    private EventHandler<WindowEvent.Close> onCloseEvent;

    private       GuiPane                 mainPanel;
    private final ArrayList<SubGuiScreen> windows;
    private       IGuiRenderer            renderer;

    private final BaseProperty<Float> widthProperty, heightProperty, xPosProperty, yPosProperty;

    private final BaseProperty<Float> xRelativePosProperty, yRelativePosProperty;

    private final BaseProperty<Integer> screenWidthProperty, screenHeightProperty;

    private final BaseListProperty<String> stylesheetsProperty;
    private final BaseProperty<String>     userAgentStylesheetProperty;
    private final BaseProperty<StyleList>  userAgentStyleTreeProperty;
    private final BaseProperty<StyleList>  styleTreeProperty;

    private IBrokkGuiImpl wrapper;

    public BrokkGuiScreen(final float xRelativePos, final float yRelativePos, final float width, final float height)
    {
        this.widthProperty = new BaseProperty<>(width, "widthProperty");
        this.heightProperty = new BaseProperty<>(height, "heightProperty");

        this.xRelativePosProperty = new BaseProperty<>(xRelativePos, "xRelativePosProperty");
        this.yRelativePosProperty = new BaseProperty<>(yRelativePos, "yRelativePosProperty");

        this.xPosProperty = new BaseProperty<>(0f, "xPosProperty");
        this.yPosProperty = new BaseProperty<>(0f, "yPosProperty");

        this.windows = new ArrayList<>();

        this.screenWidthProperty = new BaseProperty<>(0, "screenWidthProperty");
        this.screenHeightProperty = new BaseProperty<>(0, "screenHeightProperty");

        this.stylesheetsProperty = new BaseListProperty<>(Collections.emptyList(), "styleSheetsListProperty");
        this.userAgentStylesheetProperty = new BaseProperty<>(null, "userAgentStylesheetProperty");
        this.userAgentStyleTreeProperty = new BaseProperty<>(null, "userAgentStyleTreeProperty");
        this.styleTreeProperty = new BaseProperty<>(null, "styleTreeProperty");

        this.stylesheetsProperty.addListener(obs ->
        {
            StylesheetManager.getInstance().refreshStylesheets(this);
            if (this.getMainPanel() != null)
                this.getMainPanel().refreshStyle();
        });
        this.userAgentStylesheetProperty.addListener(obs ->
        {
            StylesheetManager.getInstance().refreshStylesheets(this);
            StylesheetManager.getInstance().refreshStylesheets(this);
            if (this.getMainPanel() != null)
                this.getMainPanel().refreshStyle();
        });

        this.setMainPanel(new GuiPane());
    }

    public BrokkGuiScreen(final float width, final float height)
    {
        this(0, 0, width, height);
    }

    public BrokkGuiScreen()
    {
        this(0, 0);
    }

    public void setWrapper(final IBrokkGuiImpl wrapper)
    {
        this.wrapper = wrapper;

        this.renderer = wrapper.getRenderer();

        this.xPosProperty.bind(new BaseExpression<>(() ->
                BrokkGuiScreen.this.getScreenWidth() / (1 / BrokkGuiScreen.this.getxRelativePos())
                        - BrokkGuiScreen.this.getWidth() / 2, this.getScreenWidthProperty(), this
                .getxRelativePosProperty(), this.getWidthProperty()));

        this.yPosProperty.bind(new BaseExpression<>(() ->
                BrokkGuiScreen.this.getScreenHeight() / (1 / BrokkGuiScreen.this.getyRelativePos())
                        - BrokkGuiScreen.this.getHeight() / 2, this.getyRelativePosProperty(), this
                .getScreenHeightProperty(), this.getHeightProperty()));
    }

    public void render(final int mouseX, final int mouseY, final float partialTicks)
    {
        for (final EGuiRenderPass pass : EGuiRenderPass.VALUES)
        {
            this.renderer.beginPass(pass);
            this.mainPanel.renderNode(this.renderer, pass, mouseX, mouseY);
            this.renderer.endPass(pass);
        }

        if (!this.windows.isEmpty())
            for (int i = this.windows.size() - 1; i >= 0; i--)
            {
                if (this.windows.get(i).hasWarFog())
                    this.renderer.getHelper().drawColoredRect(this.renderer, 0, 0, this.getWidth(), this.getHeight(),
                            5 + i, Color.BLACK.addAlpha(-0.5f));
                this.windows.get(i).setzLevel(5 + i);

                for (final EGuiRenderPass pass : EGuiRenderPass.VALUES)
                {
                    this.renderer.beginPass(pass);
                    this.windows.get(i).renderNode(this.renderer, pass, mouseX, mouseY);
                    this.renderer.endPass(pass);
                }
            }
    }

    public void initGui()
    {
    }

    public void onClick(final int mouseX, final int mouseY, final int key)
    {
        if (!this.windows.isEmpty())
        {
            if (this.windows.get(0).isPointInside(mouseX, mouseY))
                this.windows.get(0).handleClick(mouseX, mouseY, key);
            else if (this.windows.get(0).closeOnClick())
                this.removeSubGui(this.windows.get(0));
        }
        else if (this.mainPanel.isPointInside(mouseX, mouseY))
            this.mainPanel.handleClick(mouseX, mouseY, key);
    }

    public void handleMouseInput()
    {
        if (GuiFocusManager.getInstance().getFocusedNode() != null)
            GuiFocusManager.getInstance().getFocusedNode().handleMouseInput();
    }

    public void onKeyTyped(final char c, final int key)
    {
        if (GuiFocusManager.getInstance().getFocusedNode() != null)
            GuiFocusManager.getInstance().getFocusedNode().handleKeyInput(c, key);
    }

    public void addSubGui(final SubGuiScreen subGui)
    {
        this.windows.add(0, subGui);
        subGui.open();
    }

    public void removeSubGui(final SubGuiScreen subGui)
    {
        subGui.close();
        this.windows.remove(subGui);
    }

    public boolean hasSubGui(final SubGuiScreen subGui)
    {
        return this.windows.contains(subGui);
    }

    @Override
    public void open()
    {
        this.wrapper.askOpen();
        this.onOpen();
    }

    public void onOpen()
    {
        this.getEventDispatcher().dispatchEvent(WindowEvent.OPEN, new WindowEvent.Open(this));
    }

    @Override
    public void close()
    {
        this.wrapper.askClose();
        this.onClose();
    }

    public void onClose()
    {
        this.getEventDispatcher().dispatchEvent(WindowEvent.CLOSE, new WindowEvent.Close(this));
    }

    public GuiPane getMainPanel()
    {
        return this.mainPanel;
    }

    public void setMainPanel(final GuiPane mainPanel)
    {
        if (this.mainPanel != null)
        {
            this.mainPanel.getWidthProperty().unbind();
            this.mainPanel.getHeightProperty().unbind();
            this.mainPanel.getxPosProperty().unbind();
            this.mainPanel.getyPosProperty().unbind();
        }

        this.mainPanel = mainPanel;

        this.mainPanel.getWidthProperty().bind(this.widthProperty);
        this.mainPanel.getHeightProperty().bind(this.heightProperty);

        this.mainPanel.getxPosProperty().bind(this.xPosProperty);
        this.mainPanel.getyPosProperty().bind(this.yPosProperty);

        this.mainPanel.setStyleTree(this.getStyleTreeProperty()::getValue);
        this.mainPanel.refreshStyle();
    }

    public BaseProperty<Float> getWidthProperty()
    {
        return this.widthProperty;
    }

    public BaseProperty<Float> getHeightProperty()
    {
        return this.heightProperty;
    }

    public BaseProperty<Float> getxPosProperty()
    {
        return this.xPosProperty;
    }

    public BaseProperty<Float> getyPosProperty()
    {
        return this.yPosProperty;
    }

    public BaseProperty<Float> getxRelativePosProperty()
    {
        return this.xRelativePosProperty;
    }

    public BaseProperty<Float> getyRelativePosProperty()
    {
        return this.yRelativePosProperty;
    }

    public float getWidth()
    {
        return this.getWidthProperty().getValue();
    }

    public float getHeight()
    {
        return this.getHeightProperty().getValue();
    }

    public void setWidth(final float width)
    {
        this.getWidthProperty().setValue(width);
    }

    public void setHeight(final float height)
    {
        this.getHeightProperty().setValue(height);
    }

    public float getxPos()
    {
        return this.getxPosProperty().getValue();
    }

    public void setxPos(final float xPos)
    {
        this.getxPosProperty().setValue(xPos);
    }

    public float getyPos()
    {
        return this.getyPosProperty().getValue();
    }

    public void setyPos(final float yPos)
    {
        this.getyPosProperty().setValue(yPos);
    }

    public float getxRelativePos()
    {
        return this.getxRelativePosProperty().getValue();
    }

    public void setxRelativePos(final float xRelativePos)
    {
        this.getxRelativePosProperty().setValue(xRelativePos);
    }

    public float getyRelativePos()
    {
        return this.getyRelativePosProperty().getValue();
    }

    public void setyRelativePos(final float yRelativePos)
    {
        this.getyRelativePosProperty().setValue(yRelativePos);
    }

    public BaseProperty<Integer> getScreenWidthProperty()
    {
        return this.screenWidthProperty;
    }

    public BaseProperty<Integer> getScreenHeightProperty()
    {
        return this.screenHeightProperty;
    }

    public int getScreenWidth()
    {
        return this.getScreenWidthProperty().getValue();
    }

    public int getScreenHeight()
    {
        return this.getScreenHeightProperty().getValue();
    }

    /////////////////////
    // EVENTS HANDLING //
    /////////////////////

    public void setOnOpenEvent(final EventHandler<WindowEvent.Open> onOpenEvent)
    {
        this.getEventDispatcher().removeHandler(WindowEvent.OPEN, this.onOpenEvent);
        this.onOpenEvent = onOpenEvent;
        this.getEventDispatcher().addHandler(WindowEvent.OPEN, this.onOpenEvent);
    }

    public void setOnCloseEvent(final EventHandler<WindowEvent.Close> onCloseEvent)
    {
        this.getEventDispatcher().removeHandler(WindowEvent.CLOSE, this.onCloseEvent);
        this.onCloseEvent = onCloseEvent;
        this.getEventDispatcher().addHandler(WindowEvent.CLOSE, this.onCloseEvent);
    }

    @Override
    public EventDispatcher getEventDispatcher()
    {
        if (this.eventDispatcher == null)
            this.initEventDispatcher();
        return this.eventDispatcher;
    }

    private void initEventDispatcher()
    {
        this.eventDispatcher = new EventDispatcher();
    }

    /////////////////////
    //     STYLING     //
    /////////////////////

    public BaseListProperty<String> getStylesheetsProperty()
    {
        return stylesheetsProperty;
    }

    public BaseProperty<String> getUserAgentStylesheetProperty()
    {
        return userAgentStylesheetProperty;
    }

    private BaseProperty<StyleList> getStyleTreeProperty()
    {
        return this.styleTreeProperty;
    }

    private BaseProperty<StyleList> getUserAgentStyleTreeProperty()
    {
        return this.userAgentStyleTreeProperty;
    }

    public void setStyleTree(StyleList tree)
    {
        this.getStyleTreeProperty().setValue(tree);
    }

    public void setUserAgentStyleTree(StyleList tree)
    {
        this.getUserAgentStyleTreeProperty().setValue(tree);
    }

    public StyleList getUserAgentStyleTree()
    {
        return this.getUserAgentStyleTreeProperty().getValue();
    }

    public void addStylesheet(String stylesheetLocation)
    {
        this.getStylesheetsProperty().add(stylesheetLocation);
    }

    public void removeStylesheet(String stylesheetLocation)
    {
        this.getStylesheetsProperty().remove(stylesheetLocation);
    }

    public void setUserAgentStylesheet(String stylesheetLocation)
    {
        this.getUserAgentStylesheetProperty().setValue(stylesheetLocation);
    }
}