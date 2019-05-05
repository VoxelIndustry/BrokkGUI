package net.voxelindustry.brokkgui.component;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransformTest
{
    @Test
    void parentCheck()
    {
        GuiElement parent = new TestElement();

        parent.transform().xPosProperty().setValue(50f);
        parent.transform().yPosProperty().setValue(50f);

        GuiElement child = new TestElement();
        parent.transform().addChild(child.transform());

        assertThat(child.transform().xPos()).isEqualTo(50);
        assertThat(child.transform().yPos()).isEqualTo(50);
    }
}
