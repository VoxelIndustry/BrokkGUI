package fr.ourten.brokkgui.demo;

import java.util.Arrays;

import fr.ourten.brokkgui.component.GuiTab;
import fr.ourten.brokkgui.element.GuiButton;
import fr.ourten.brokkgui.element.GuiListView;
import fr.ourten.brokkgui.element.GuiTextfield;
import fr.ourten.brokkgui.gui.BrokkGuiScreen;
import fr.ourten.brokkgui.paint.Background;
import fr.ourten.brokkgui.paint.Color;
import fr.ourten.brokkgui.panel.GuiRelativePane;
import fr.ourten.brokkgui.panel.GuiTabPane;
import fr.ourten.brokkgui.skin.GuiButtonSkin;

public class GuiDemo extends BrokkGuiScreen
{
    public GuiDemo()
    {
        super(0.5f, 0.5f, 200, 200);

        final GuiRelativePane pane = new GuiRelativePane();
        this.setMainPanel(pane);
        final GuiButton button = new GuiButton("Test Button");

        button.setWidth(200);
        button.setHeight(30);

        button.getSkin().setBorderColor(Color.GREEN);
        button.getSkin().setBorderThin(2);

        final Background background = new Background(Color.AQUA);
        final Background hoverBack = new Background(Color.YELLOW);
        final Background disableBack = new Background(Color.RED);

        ((GuiButtonSkin) button.getSkin()).setBackground(background);
        ((GuiButtonSkin) button.getSkin()).setHoveredBackground(hoverBack);
        ((GuiButtonSkin) button.getSkin()).setDisabledBackground(disableBack);

        final GuiButton b2 = new GuiButton("B2");

        ((GuiButtonSkin) b2.getSkin()).setBackground(background);
        ((GuiButtonSkin) b2.getSkin()).setHoveredBackground(hoverBack);
        ((GuiButtonSkin) b2.getSkin()).setDisabledBackground(disableBack);

        button.setOnClickEvent(e -> System.out.println("clicked"));

        final GuiListView<String> listView = new GuiListView<>(Arrays.asList("test1", "test3", "teeeeest"));

        listView.setWidth(100);
        listView.setHeight(30);

        listView.setCellHeight(20);
        listView.setCellWidth(100);

        listView.getSkin().setBorderColor(Color.GRAY);
        listView.getSkin().setBorderThin(1);

        // pane.addChild(listView, 0.7f, 0.5f);

        final GuiTextfield field = new GuiTextfield();
        field.setWidth(200);
        field.setHeight(40);

        field.getSkin().setBorderColor(Color.BLACK);
        field.getSkin().setBorderThin(1);

        final GuiTabPane tabPane = new GuiTabPane();
        tabPane.setWidth(150);
        tabPane.setHeight(200);
        tabPane.getSkin().setBorderColor(Color.YELLOW);
        tabPane.getSkin().setBorderThin(1);
        tabPane.addTab(new GuiTab("Textfield", field));
        tabPane.addTab(new GuiTab("Button", listView));
        tabPane.setDefaultTab(0);

        pane.addChild(tabPane);

        this.getMainPanel().setBorderColor(Color.AQUA);
        this.getMainPanel().setBorderThin(1);
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }
}