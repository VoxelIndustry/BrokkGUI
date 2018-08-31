package org.yggard.brokkgui.demo;

import fr.ourten.teabeans.binding.BaseBinding;
import org.yggard.brokkgui.component.GuiTab;
import org.yggard.brokkgui.component.ToastManager;
import org.yggard.brokkgui.data.EAlignment;
import org.yggard.brokkgui.data.EHAlignment;
import org.yggard.brokkgui.data.RectOffset;
import org.yggard.brokkgui.demo.category.*;
import org.yggard.brokkgui.element.GuiButton;
import org.yggard.brokkgui.element.GuiLabel;
import org.yggard.brokkgui.element.GuiProgressBar;
import org.yggard.brokkgui.gui.BrokkGuiScreen;
import org.yggard.brokkgui.paint.Texture;
import org.yggard.brokkgui.panel.GuiRelativePane;
import org.yggard.brokkgui.panel.GuiTabPane;

public class GuiDemo extends BrokkGuiScreen
{
    public ToastManager toastManager;

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

        final GuiTabPane tabPane = new GuiTabPane();
        tabPane.setWidth(200);
        tabPane.setHeight(200);
        tabPane.addTab(new GuiTab("Textfield", new TextFieldDemo()));
        tabPane.addTab(new GuiTab("ListView", new ListViewDemo(this)));
        tabPane.addTab(new GuiTab("Progress", progressBar));
        tabPane.addTab(new GuiTab("RadioButton", new RadioButtonDemo()));
        tabPane.addTab(new GuiTab("Labels", new LabelDemo()));
        tabPane.addTab(new GuiTab("Scroll", new ScrollDemo()));

        tabPane.setDefaultTab(5);

        pane.addChild(tabPane);

        this.getMainPanel().setID("mainpane");

        this.toastManager = new ToastManager(this);
        toastManager.setRelativeXPos(0.5f);
        toastManager.setRelativeYPos(0.98f);
        toastManager.setToastAlignment(EAlignment.MIDDLE_UP);

        GuiLabel label = new GuiLabel("Toast");
        label.addStyleClass("toast-label");
        label.setTextPadding(new RectOffset(2, 0, 2, 0));
        label.setWidth(150);
        label.setHeight(20);
        toastManager.addToast(label, 5_000L);
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }
}
