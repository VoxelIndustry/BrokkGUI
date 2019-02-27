package net.voxelindustry.brokkgui.demo.category;

import net.voxelindustry.brokkgui.animation.transition.TranslateTransition;
import net.voxelindustry.brokkgui.control.GuiButtonBase;
import net.voxelindustry.brokkgui.element.input.GuiRadioButton;
import net.voxelindustry.brokkgui.panel.GuiRelativePane;

import java.util.concurrent.TimeUnit;

public class AnimationDemo extends GuiRelativePane
{
    public AnimationDemo()
    {
        GuiButtonBase movingButton = new GuiRadioButton("GO LEFT");
        movingButton.setExpandToLabel(true);
        movingButton.setHeight(15);

        TranslateTransition leftTransition = new TranslateTransition(movingButton, 3, TimeUnit.SECONDS);
        leftTransition.setMaxCycles(2);
        leftTransition.setReverse(true);

        leftTransition.setTranslateX(100);
        leftTransition.setTranslateY(60);

        movingButton.setOnActionEvent(e ->
        {
            if (leftTransition.isRunning())
                leftTransition.reset();
            else
                leftTransition.restart();
        });
        this.addChild(movingButton, 0, 0.5f);
    }
}
