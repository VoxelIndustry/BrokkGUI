package org.yggard.brokkgui.demo;

import org.yggard.brokkgui.component.GuiTab;
import org.yggard.brokkgui.control.GuiToggleGroup;
import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.data.EHAlignment;
import org.yggard.brokkgui.element.GuiButton;
import org.yggard.brokkgui.element.GuiCheckbox;
import org.yggard.brokkgui.element.GuiLabel;
import org.yggard.brokkgui.element.GuiListView;
import org.yggard.brokkgui.element.GuiProgressBar;
import org.yggard.brokkgui.element.GuiRadioButton;
import org.yggard.brokkgui.element.GuiTextfield;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.paint.Background;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.paint.Texture;
import org.yggard.brokkgui.panel.GuiRelativePane;
import org.yggard.brokkgui.panel.GuiTabPane;
import org.yggard.brokkgui.skin.GuiButtonSkin;

import fr.ourten.teabeans.binding.BaseBinding;

public class GuiDemo extends BrokkGuiScreen
{
    public GuiDemo()
    {
        super(0.5f, 0.5f, 200, 200);

        final GuiRelativePane pane = new GuiRelativePane();
        this.setMainPanel(pane);

        pane.setBackground(new Background(new Texture("brokkguidemo:textures/gui/background.png")));
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

        final GuiListView<String> listView = new GuiListView<>();

        listView.setWidth(100);
        listView.setHeight(30);

        listView.setCellHeight(20);
        listView.setCellWidth(100);

        listView.getSkin().setBorderColor(Color.GRAY);
        listView.getSkin().setBorderThin(1);

        listView.setPlaceholder(new GuiLabel("I'm a placeholder"));

        final GuiProgressBar progressBar = new GuiProgressBar(0.5f);
        progressBar.setProgressDirection(EHAlignment.CENTER);
        progressBar.setWidth(200);
        progressBar.getTextProperty().bind(new BaseBinding<String>()
        {
            {
                super.bind(progressBar.getProgressProperty());
            }

            @Override
            public String computeValue()
            {
                return progressBar.getProgress() + "";
            }
        });

        progressBar.setHeight(20);
        progressBar.getSkin().setBorderColor(Color.RED);
        progressBar.getSkin().setBorderThin(1);

        final GuiTextfield field = new GuiTextfield();
        field.setWidth(200);
        field.setHeight(40);

        field.getSkin().setBorderColor(Color.BLACK);
        field.getSkin().setBorderThin(1);

        final GuiRelativePane radioButtonPane = new GuiRelativePane();
        radioButtonPane.setBackground(new Background(Color.LIGHT_GRAY));

        final GuiRadioButton radioButton = new GuiRadioButton("Right 1");
        final GuiRadioButton radioButton2 = new GuiRadioButton("Nothing to see here 2");
        final GuiCheckbox checkbox = new GuiCheckbox("Left 3");

        radioButton.setHeightRatio(0.05f);

        radioButton2.setHeightRatio(0.05f);

        checkbox.setHeightRatio(0.05f);
        checkbox.setLabelAlignment(EHAlignment.LEFT);

        final GuiToggleGroup toggleGroup = new GuiToggleGroup();
        toggleGroup.setAllowNothing(true);

        toggleGroup.addButtons(radioButton, radioButton2, checkbox);

        radioButtonPane.addChild(radioButton, 0.5f, 0.2f);
        radioButtonPane.addChild(radioButton2, 0.5f, 0.4f);
        radioButtonPane.addChild(checkbox, 0.5f, 0.6f);

        final GuiRelativePane labelPane = new GuiRelativePane();

        final GuiLabel left = new GuiLabel("LEFT-CENTER");
        left.setTextAlignment(EAlignment.LEFT_CENTER);

        final GuiLabel center = new GuiLabel("CENTER");
        center.setTextAlignment(EAlignment.MIDDLE_CENTER);

        final GuiLabel right = new GuiLabel("RIGHT");
        right.setTextAlignment(EAlignment.RIGHT_CENTER);

        labelPane.addChild(left, 0, 0.5f);
        labelPane.addChild(center, 0.5f, 0.5f);
        labelPane.addChild(right, 1, 0.5f);
        labelPane.setBackground(new Background(new Color(1, 0, 0, 0.5f)));

        final GuiTabPane tabPane = new GuiTabPane();
        tabPane.setWidth(190);
        tabPane.setHeight(200);
        tabPane.getSkin().setBorderColor(Color.YELLOW);
        tabPane.getSkin().setBorderThin(1);
        tabPane.addTab(new GuiTab("Textfield", field));
        tabPane.addTab(new GuiTab("ListView", listView));
        tabPane.addTab(new GuiTab("Progress", progressBar));
        tabPane.addTab(new GuiTab("RadioButton", radioButtonPane));
        tabPane.addTab(new GuiTab("Labels", labelPane));

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