package net.voxelindustry.brokkgui;

import net.voxelindustry.brokkgui.layout.LayoutAlignment;
import net.voxelindustry.brokkgui.layout.LayoutBox;
import net.voxelindustry.brokkgui.layout.LayoutGroup;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LayoutGroupTest
{
    @Test
    void layoutDimension_givenTwoBoxesHorizontallyAligned_withSameHeight_thenShouldSumConstraint()
    {
        final LayoutBox left = new LayoutBox(10, 15, 10, 15, 10, 15);
        final LayoutBox right = new LayoutBox(15, 15, 15, 15, 15, 15);

        final LayoutGroup group = new LayoutGroup(left, right, LayoutAlignment.LEFT_CENTER);

        group.markDirty();

        assertThat(group.minWidth()).isEqualTo(25);
        assertThat(group.minHeight()).isEqualTo(15);
    }

    @Test
    void layoutDimension_givenTwoBoxesVerticallyAligned_withSameWidth_thenShouldSumConstraint()
    {
        final LayoutBox left = new LayoutBox(10, 15, 10, 15, 10, 15);
        final LayoutBox right = new LayoutBox(10, 15, 10, 15, 10, 15);

        final LayoutGroup group = new LayoutGroup(left, right, LayoutAlignment.CENTER_TOP);

        group.markDirty();

        assertThat(group.minWidth()).isEqualTo(10);
        assertThat(group.minHeight()).isEqualTo(30);
    }

    @Test
    void layoutDimension_givenTwoBoxesAtCorners_withDifferentDimensions_thenShouldSumConstraint()
    {
        final LayoutBox left = new LayoutBox(10, 15, 10, 15, 10, 15);
        final LayoutBox right = new LayoutBox(15, 20, 15, 20, 15, 20);

        final LayoutGroup group = new LayoutGroup(left, right, LayoutAlignment.LEFT_TOP);

        group.markDirty();

        assertThat(group.minWidth()).isEqualTo(25);
        assertThat(group.minHeight()).isEqualTo(35);
    }

    @Test
    void layoutDimension_givenSizeEqualToMax_thenShouldUseMax()
    {
        final LayoutBox left = new LayoutBox(10, 15, 10, 15, 20, 15);
        final LayoutBox right = new LayoutBox(10, 15, 10, 15, 20, 20);

        final LayoutGroup group = new LayoutGroup(left, right, LayoutAlignment.LEFT_TOP);
        group.layoutWidth(40);
        group.layoutHeight(40);
        group.markDirty();

        assertThat(group.effectiveWidth()).isEqualTo(left.maxWidth() + right.maxWidth());
        assertThat(group.effectiveHeight()).isEqualTo(left.maxHeight() + right.maxHeight());
    }

    @Test
    void layoutDimension_givenLargerSizeThanPreferred_andBelowMax_thenShouldGrowToAvailableSize()
    {
        final LayoutBox left = new LayoutBox(10, 10, 10, 10, 20, 20);
        final LayoutBox right = new LayoutBox(10, 10, 10, 10, 20, 20);

        final LayoutGroup group = new LayoutGroup(left, right, LayoutAlignment.LEFT_TOP);
        group.layoutWidth(30);
        group.layoutHeight(30);
        group.markDirty();

        assertThat(group.effectiveWidth()).isEqualTo(group.layoutWidth()).isEqualTo(30);
        assertThat(group.effectiveHeight()).isEqualTo(group.layoutHeight()).isEqualTo(30);
    }

    @Test
    void childrenWidth_givenSmallerThanPreferred_thenShouldShrinkSecondFirst()
    {
        final LayoutBox left = new LayoutBox(5, 5, 20, 20, 20, 20);
        final LayoutBox right = new LayoutBox(5, 5, 20, 20, 20, 20);

        final LayoutGroup group = new LayoutGroup(left, right, LayoutAlignment.LEFT_TOP);
        group.layoutWidth(15);
        group.layoutHeight(15);
        group.markDirty();

        assertThat(group.firstWidth()).isEqualTo(10);
        assertThat(group.secondWidth()).isEqualTo(5);

        assertThat(group.firstHeight()).isEqualTo(10);
        assertThat(group.secondHeight()).isEqualTo(5);
    }

    @Test
    void childrenWidth_givenLargerThanPreferred_thenShouldExpandFirst()
    {
        final LayoutBox left = new LayoutBox(5, 5, 10, 10, 20, 20);
        final LayoutBox right = new LayoutBox(5, 5, 10, 10, 20, 20);

        final LayoutGroup group = new LayoutGroup(left, right, LayoutAlignment.LEFT_TOP);
        group.layoutWidth(30);
        group.layoutHeight(30);
        group.markDirty();

        assertThat(group.firstWidth()).isEqualTo(20);
        assertThat(group.secondWidth()).isEqualTo(10);

        assertThat(group.firstHeight()).isEqualTo(20);
        assertThat(group.secondHeight()).isEqualTo(10);
    }

    @Test
    void childrenWidth_givenEnoughForMax_thenShouldExpandBothToMax()
    {

        final LayoutBox left = new LayoutBox(5, 5, 10, 10, 20, 20);
        final LayoutBox right = new LayoutBox(5, 5, 10, 10, 20, 20);

        final LayoutGroup group = new LayoutGroup(left, right, LayoutAlignment.LEFT_TOP);
        group.layoutWidth(40);
        group.layoutHeight(40);
        group.markDirty();

        assertThat(group.firstWidth()).isEqualTo(20);
        assertThat(group.secondWidth()).isEqualTo(20);

        assertThat(group.firstHeight()).isEqualTo(20);
        assertThat(group.secondHeight()).isEqualTo(20);
    }

    @Test
    void childrenWidth_givenOnlyForMin_thenShouldShrinkBothToMin()
    {
        final LayoutBox left = new LayoutBox(5, 5, 10, 10, 20, 20);
        final LayoutBox right = new LayoutBox(5, 5, 10, 10, 20, 20);

        final LayoutGroup group = new LayoutGroup(left, right, LayoutAlignment.LEFT_TOP);
        group.layoutWidth(10);
        group.layoutHeight(10);
        group.markDirty();

        assertThat(group.firstWidth()).isEqualTo(5);
        assertThat(group.secondWidth()).isEqualTo(5);

        assertThat(group.firstHeight()).isEqualTo(5);
        assertThat(group.secondHeight()).isEqualTo(5);
    }

    @Test
    void childrenPos_givenPerfectFit_withCornerAlignment_thenShouldPlaceFirstAtCornerAndSecondAtOpposite()
    {
        final LayoutBox left = new LayoutBox(5, 5, 5, 5, 5, 5);
        final LayoutBox right = new LayoutBox(5, 5, 5, 5, 5, 5);

        final LayoutGroup group = new LayoutGroup(left, right, LayoutAlignment.LEFT_TOP);
        group.layoutWidth(10);
        group.layoutHeight(10);
        group.markDirty();

        assertThat(group.firstPosX()).isEqualTo(0);
        assertThat(group.firstPosY()).isEqualTo(0);

        assertThat(group.secondPosX()).isEqualTo(5);
        assertThat(group.secondPosY()).isEqualTo(5);
    }

    @Test
    void childrenPos_givenPerfectFit_withCenteredAlignment_thenShouldPlaceFirstCenteredTopAndSecondBelow()
    {
        final LayoutBox first = new LayoutBox(5, 5, 5, 5, 5, 5);
        final LayoutBox second = new LayoutBox(10, 10, 10, 10, 10, 10);

        final LayoutGroup group = new LayoutGroup(first, second, LayoutAlignment.CENTER_TOP);
        group.layoutWidth(10);
        group.layoutHeight(15);
        group.markDirty();

        assertThat(group.firstPosX()).isEqualTo(2.5F);
        assertThat(group.firstPosY()).isEqualTo(0);

        assertThat(group.secondPosX()).isEqualTo(0);
        assertThat(group.secondPosY()).isEqualTo(5);
    }

    @Test
    void childrenPos_givenPerfectFit_withLeftCenteredAlignment_thenShouldPlaceFirstVerticallyCentered()
    {
        final LayoutBox first = new LayoutBox(5, 5, 5, 5, 5, 5);
        final LayoutBox second = new LayoutBox(10, 10, 10, 10, 10, 10);

        final LayoutGroup group = new LayoutGroup(first, second, LayoutAlignment.LEFT_CENTER);
        group.layoutWidth(15);
        group.layoutHeight(10);
        group.markDirty();

        assertThat(group.firstPosX()).isEqualTo(0);
        assertThat(group.firstPosY()).isEqualTo(2.5F);

        assertThat(group.secondPosX()).isEqualTo(5);
        assertThat(group.secondPosY()).isEqualTo(0);
    }
}
