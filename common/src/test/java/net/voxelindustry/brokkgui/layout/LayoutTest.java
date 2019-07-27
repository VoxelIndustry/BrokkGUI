package net.voxelindustry.brokkgui.layout;

import net.voxelindustry.brokkgui.data.RectAlignment;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LayoutTest
{
    @Test
    void refresh_givenOneBoxWithEnoughSpace_thenShouldDoNothing()
    {
        var layout = new Layout();

        var component = new DummyLayoutComponentBuilder()
                .setPos(0, 0)
                .setSize(10, 10)
                .setWidth(10)
                .setHeight(10)
                .setUnshrinkableTo(10, 10)
                .createDummyLayoutComponent();

        layout.addLayout(component);

        layout.refresh(10, 10);

        assertThat(component.xPos()).isEqualTo(0);
        assertThat(component.yPos()).isEqualTo(0);

        assertThat(component.width()).isEqualTo(10);
        assertThat(component.height()).isEqualTo(10);
    }

    @Test
    void refresh_givenTwoBoxesWithEnoughSpace_thenShouldHorizontallyStackThem()
    {
        var layout = new Layout();

        var componentLeft = new DummyLayoutComponentBuilder()
                .setPos(0, 0)
                .setSize(10, 10)
                .setWidth(10)
                .setHeight(10)
                .setUnshrinkableTo(10, 10)
                .setAlignment(RectAlignment.LEFT_CENTER)
                .createDummyLayoutComponent();

        var componentRight = new DummyLayoutComponentBuilder()
                .setPos(0, 0)
                .setSize(10, 10)
                .setWidth(10)
                .setHeight(10)
                .setUnshrinkableTo(10, 10)
                .setAlignment(RectAlignment.RIGHT_CENTER)
                .createDummyLayoutComponent();

        layout.addLayout(componentRight);
        layout.addLayout(componentLeft);

        layout.refresh(20, 10);

        assertThat(componentRight.xPos()).isEqualTo(0);
        assertThat(componentRight.yPos()).isEqualTo(0);

        assertThat(componentRight.width()).isEqualTo(10);
        assertThat(componentRight.height()).isEqualTo(10);
    }

    @Test
    void refresh_givenThreeBoxesWithPrioritiesAndEnoughSpace_thenShouldHorizontallyStackAndSortThem()
    {

    }

    @Test
    void refresh_givenTwoBoxesWithOneShrinkableAndOneImmovableWithNotEnoughSpace_thenShouldResizeTheShrinkable()
    {

    }

}
