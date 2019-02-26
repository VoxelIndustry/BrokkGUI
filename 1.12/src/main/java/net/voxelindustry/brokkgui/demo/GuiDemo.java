package net.voxelindustry.brokkgui.demo;

import net.voxelindustry.brokkgui.data.RectAlignment;
import net.voxelindustry.brokkgui.data.RectOffset;
import net.voxelindustry.brokkgui.demo.category.*;
import net.voxelindustry.brokkgui.element.GuiLabel;
import net.voxelindustry.brokkgui.element.ToastManager;
import net.voxelindustry.brokkgui.element.input.GuiButton;
import net.voxelindustry.brokkgui.element.pane.GuiTab;
import net.voxelindustry.brokkgui.element.pane.GuiTabPane;
import net.voxelindustry.brokkgui.gui.BrokkGuiScreen;
import net.voxelindustry.brokkgui.paint.Texture;
import net.voxelindustry.brokkgui.panel.GuiRelativePane;

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
        button.setStyle("border-color: green; border-width: 2; text-color: khaki");

        button.setOnClickEvent(e -> System.out.println("clicked"));

        final GuiTabPane tabPane = new GuiTabPane();
        tabPane.setWidth(200);
        tabPane.setHeight(200);
        tabPane.addTab(new GuiTab("Textfield", new TextFieldDemo()));
        tabPane.addTab(new GuiTab("ListView", new ListViewDemo(this)));
        tabPane.addTab(new GuiTab("Animation", new AnimationDemo()));
        tabPane.addTab(new GuiTab("RadioButton", new RadioButtonDemo()));
        tabPane.addTab(new GuiTab("Labels", new LabelDemo()));
        tabPane.addTab(new GuiTab("Scroll", new ScrollDemo()));

        tabPane.setDefaultTab(5);

        pane.addChild(tabPane);

        this.getMainPanel().setID("mainpane");

        this.toastManager = new ToastManager(this);
        toastManager.setRelativeXPos(0.5f);
        toastManager.setRelativeYPos(0.98f);
        toastManager.setToastAlignment(RectAlignment.MIDDLE_UP);

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
