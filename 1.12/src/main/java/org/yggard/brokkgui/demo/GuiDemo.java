package org.yggard.brokkgui.demo;

import fr.ourten.teabeans.binding.BaseBinding;
import org.yggard.brokkgui.component.GuiTab;
import org.yggard.brokkgui.data.EHAlignment;
import org.yggard.brokkgui.demo.category.LabelDemo;
import org.yggard.brokkgui.demo.category.ListViewDemo;
import org.yggard.brokkgui.demo.category.RadioButtonDemo;
import org.yggard.brokkgui.element.GuiButton;
import org.yggard.brokkgui.element.GuiProgressBar;
import org.yggard.brokkgui.element.GuiTextfield;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.paint.Texture;
import org.yggard.brokkgui.panel.GuiRelativePane;
import org.yggard.brokkgui.panel.GuiTabPane;

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
        button.setStyle("-border-color: green; -border-thin: 2; -text-color: khaki");

        button.setOnClickEvent(e -> System.out.println("clicked"));

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
        progressBar.setStyle("-border-color: red; -border-thin: 2;");

        final GuiTextfield field = new GuiTextfield();
        field.setWidth(200);
        field.setHeight(40);

        field.setStyle("-border-color: black; -border-thin: 1;");

        final GuiTabPane tabPane = new GuiTabPane();
        tabPane.setWidth(190);
        tabPane.setHeight(200);
        tabPane.setStyle("-border-color: yellow; -border-thin: 1;");
        tabPane.addTab(new GuiTab("Textfield", field));
        tabPane.addTab(new GuiTab("ListView", new ListViewDemo()));
        tabPane.addTab(new GuiTab("Progress", progressBar));
        tabPane.addTab(new GuiTab("RadioButton", new RadioButtonDemo()));
        tabPane.addTab(new GuiTab("Labels", new LabelDemo()));

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