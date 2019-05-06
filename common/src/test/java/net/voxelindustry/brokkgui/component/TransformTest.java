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

        assertThat(child.transform().parent()).isEqualTo(parent.transform());

        parent.transform().removeChild(child.transform());

        // Pos should not change on parent removal
        assertThat(child.transform().xPos()).isEqualTo(50);
        assertThat(child.transform().yPos()).isEqualTo(50);

        assertThat(child.transform().parent()).isNull();
    }

    @Test
    void basicBounds()
    {
        GuiElement parent = new TestElement();

        parent.transform().xPosProperty().setValue(50f);
        parent.transform().yPosProperty().setValue(50f);

        parent.transform().width(50);
        parent.transform().height(50);

        assertThat(parent.transform().isPointInside(10, 10)).isFalse();
        assertThat(parent.transform().isPointInside(90, 90)).isTrue();

        GuiElement child = new TestElement();
        child.transform().width(20);
        child.transform().height(20);
        parent.transform().addChild(child.transform());

        assertThat(child.transform().isPointInside(90, 90)).isFalse();
        assertThat(child.transform().isPointInside(60, 60)).isTrue();
    }

    @Test
    void relativeSize()
    {
        GuiElement parent = new TestElement();

        parent.size(50, 50);

        GuiElement child = new TestElement();
        parent.transform().addChild(child.transform());
        child.transform().widthRatio(0.1f);
        child.transform().heightRatio(0.1f);

        assertThat(child.width()).isEqualTo(parent.width() / 10);
        assertThat(child.height()).isEqualTo(parent.height() / 10);

        GuiElement child2 = new TestElement();
        child2.transform().widthRatio(0.1f);
        child2.transform().heightRatio(0.1f);

        parent.transform().addChild(child2.transform());

        assertThat(child2.width()).isEqualTo(parent.width() / 10);
        assertThat(child2.height()).isEqualTo(parent.height() / 10);
    }
}
