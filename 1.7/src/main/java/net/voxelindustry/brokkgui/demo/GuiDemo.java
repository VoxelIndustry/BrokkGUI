package net.voxelindustry.brokkgui.demo;

import fr.ourten.teabeans.binding.BaseBinding;
import net.voxelindustry.brokkgui.component.GuiTab;
import net.voxelindustry.brokkgui.control.GuiToggleGroup;
import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectSide;
import net.voxelindustry.brokkgui.element.*;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.paint.Color;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.panel.GuiAbsolutePane;
import net.voxelindustry.brokkgui.panel.GuiRelativePane;
import net.voxelindustry.brokkgui.panel.GuiTabPane;

public class GuiDemo extends BrokkGuiScreen
{
    public GuiDemo()
    {
        super(0.5f, 0.5f, 200, 200);

        this.addStylesheet("/assets/brokkguidemo/gui/css/demo.css");

        final GuiRelativePane pane = new GuiRelativePane();
        this.setMainPanel(pane);

        pane.setBackgroundTexture(new Texture("brokkguidemo:textures/gui/background.png"));
        final GuiButton button = new GuiButton("Test Button");

        button.setWidth(200);
        button.setHeight(30);
        button.setStyle("border-color: green; border-thin: 2; text-color: khaki");

        button.setOnClickEvent(e -> System.out.println("clicked"));

        final GuiListView<String> listView = new GuiListView<>();

        listView.setWidth(100);
        listView.setHeight(30);

        listView.setCellHeight(20);
        listView.setCellWidth(100);
        listView.setStyle("border-color: gray; border-thin: 1;");

        listView.setPlaceholder(new GuiLabel("I'm a placeholder"));

        final GuiProgressBar progressBar = new GuiProgressBar(0.5f);
        progressBar.setProgressDirection(RectSide.RIGHT);
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
        progressBar.setStyle("border-color: red; border-thin: 2;");

        final GuiTextfield field = new GuiTextfield();
        field.setWidth(200);
        field.setHeight(40);

        field.setStyle("border-color: black; border-thin: 1;");

        final GuiRelativePane radioButtonPane = new GuiRelativePane();
        radioButtonPane.setBackgroundColor(Color.LIGHT_GRAY);

        final GuiRadioButton radioButton = new GuiRadioButton("Right 1");
        final GuiRadioButton radioButton2 = new GuiRadioButton("Nothing to see here 2");
        final GuiCheckbox checkbox = new GuiCheckbox("Left 3");

        radioButton.setHeightRatio(0.05f);

        radioButton2.setHeightRatio(0.05f);
        radioButton2.setStyle("text-color: green");

        checkbox.setHeightRatio(0.05f);
        checkbox.setButtonSide(RectSide.LEFT);

        final GuiToggleGroup toggleGroup = new GuiToggleGroup();
        toggleGroup.setAllowNothing(true);

        toggleGroup.addButtons(radioButton, radioButton2, checkbox);

        radioButtonPane.addChild(radioButton, 0.5f, 0.2f);
        radioButtonPane.addChild(radioButton2, 0.5f, 0.4f);
        radioButtonPane.addChild(checkbox, 0.5f, 0.6f);

        final GuiAbsolutePane labelPane = new GuiAbsolutePane();
        labelPane.setOpacity(0.5);

        final GuiLabel left = new GuiLabel("LEFT-CENTER");
        left.setTextAlignment(RectAlignment.LEFT_CENTER);

        final GuiLabel center = new GuiLabel("CENTER");
        center.setTextAlignment(RectAlignment.MIDDLE_CENTER);

        final GuiLabel right = new GuiLabel("RIGHT");
        right.setTextAlignment(RectAlignment.RIGHT_CENTER);

        labelPane.addChild(left, 0, 95);
        labelPane.addChild(center, 100, 95);
        labelPane.addChild(right, 200, 95);
        labelPane.setBackgroundColor(new Color(1, 0, 0));

        final GuiTabPane tabPane = new GuiTabPane();
        tabPane.setWidth(190);
        tabPane.setHeight(200);
        tabPane.setStyle("border-color: yellow; border-thin: 1;");
        tabPane.addTab(new GuiTab("Textfield", field));
        tabPane.addTab(new GuiTab("ListView", listView));
        tabPane.addTab(new GuiTab("Progress", progressBar));
        tabPane.addTab(new GuiTab("RadioButton", radioButtonPane));
        tabPane.addTab(new GuiTab("Labels", labelPane));

        tabPane.setDefaultTab(0);

        pane.addChild(tabPane);

        this.getMainPanel().setID("mainpane");
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }
}