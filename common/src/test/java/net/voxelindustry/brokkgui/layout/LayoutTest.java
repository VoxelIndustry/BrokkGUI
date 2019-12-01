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
                .setUnshrinkableTo(10, 10)
                .setAlignment(RectAlignment.LEFT_CENTER)
                .createDummyLayoutComponent();

        var componentRight = new DummyLayoutComponentBuilder()
                .setPos(0, 0)
                .setSize(10, 10)
                .setUnshrinkableTo(10, 10)
                .setAlignment(RectAlignment.RIGHT_CENTER)
                .createDummyLayoutComponent();

        layout.addLayout(componentRight);
        layout.addLayout(componentLeft);

        layout.refresh(20, 10);

        assertThat(componentLeft.xPos()).isEqualTo(0);
        assertThat(componentLeft.yPos()).isEqualTo(0);

        assertThat(componentLeft.width()).isEqualTo(10);
        assertThat(componentLeft.height()).isEqualTo(10);

        assertThat(componentRight.xPos()).isEqualTo(10);
        assertThat(componentRight.yPos()).isEqualTo(10);

        assertThat(componentRight.width()).isEqualTo(10);
        assertThat(componentRight.height()).isEqualTo(10);
    }

    @Test
    void refresh_givenThreeBoxesWithPrioritiesAndEnoughSpace_thenShouldHorizontallyStackAndSortThem()
    {
        var layout = new Layout();

        var componentLeft = new DummyLayoutComponentBuilder()
                .setPos(0, 0)
                .setSize(10, 10)
                .setUnshrinkableTo(10, 10)
                .setAlignment(RectAlignment.LEFT_CENTER)
                .createDummyLayoutComponent();

        var componentMiddle = new DummyLayoutComponentBuilder()
                .setPos(0, 0)
                .setSize(10, 10)
                .setUnshrinkableTo(10, 10)
                // Set it to right to test the insertion priority
                .setAlignment(RectAlignment.RIGHT_CENTER)
                .createDummyLayoutComponent();

        var componentRight = new DummyLayoutComponentBuilder()
                .setPos(0, 0)
                .setSize(10, 10)
                .setUnshrinkableTo(10, 10)
                .setAlignment(RectAlignment.RIGHT_CENTER)
                .createDummyLayoutComponent();

        layout.addLayout(componentRight);
        layout.addLayout(componentMiddle);
        layout.addLayout(componentLeft);

        layout.refresh(30, 10);

        assertThat(componentLeft.xPos()).isEqualTo(0);
        assertThat(componentLeft.yPos()).isEqualTo(0);

        assertThat(componentLeft.width()).isEqualTo(10);
        assertThat(componentLeft.height()).isEqualTo(10);

        assertThat(componentMiddle.xPos()).isEqualTo(10);
        assertThat(componentMiddle.yPos()).isEqualTo(10);

        assertThat(componentMiddle.width()).isEqualTo(10);
        assertThat(componentMiddle.height()).isEqualTo(10);

        assertThat(componentRight.xPos()).isEqualTo(20);
        assertThat(componentRight.yPos()).isEqualTo(20);

        assertThat(componentRight.width()).isEqualTo(10);
        assertThat(componentRight.height()).isEqualTo(10);
    }

    @Test
    void refresh_givenTwoBoxesWithOneShrinkableAndOneImmovableWithNotEnoughSpace_thenShouldResizeTheShrinkable()
    {
        var layout = new Layout();

        var componentLeft = new DummyLayoutComponentBuilder()
                .setPos(0, 0)
                .setSize(10, 10)
                .setUnshrinkableTo(10, 10)
                .setAlignment(RectAlignment.LEFT_CENTER)
                .createDummyLayoutComponent();

        var componentRight = new DummyLayoutComponentBuilder()
                .setPos(0, 0)
                .setSize(20, 210)
                .setMinSize(10, 10)
                .setPrefSize(20, 10)
                .setMaxSize(20, 20)
                .setAlignment(RectAlignment.RIGHT_CENTER)
                .createDummyLayoutComponent();

        layout.addLayout(componentRight);
        layout.addLayout(componentLeft);

        layout.refresh(25, 10);

        assertThat(componentLeft.xPos()).isEqualTo(0);
        assertThat(componentLeft.yPos()).isEqualTo(0);

        assertThat(componentLeft.width()).isEqualTo(10);
        assertThat(componentLeft.height()).isEqualTo(10);

        assertThat(componentRight.xPos()).isEqualTo(10);
        assertThat(componentRight.yPos()).isEqualTo(10);

        assertThat(componentRight.width()).isEqualTo(15);
        assertThat(componentRight.height()).isEqualTo(10);
    }

}
