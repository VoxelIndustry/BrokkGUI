package net.voxelindustry.brokkgui.debug.hierarchy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class AccordionLayoutTest
{
    @Test
    void moveDown_givenSimpleResizableBottom_thenShouldResizeBottom()
    {
        AccordionItem bottom = new MockAccordionItem(40, 10, 20, false);
        AccordionItem middle = new MockAccordionItem(30, 10, 20, true);
        AccordionItem top = new MockAccordionItem(0, 10, 20, false);

        AccordionLayout.moveDown(20, 100, bottom, asList(top, middle, bottom));

        assertThat(bottom.getHeaderPos()).isEqualTo(60);
    }

    // FIXME : Is the test broken or the accordion layout had an unkown behavior change?
    @Test
    @Disabled
    void moveDown_givenTwoMovableElement_thenShouldResizeBoth()
    {
        AccordionItem bottom = new MockAccordionItem(90, 10, 20, false);
        AccordionItem firstMiddle = new MockAccordionItem(60, 10, 20, true);
        AccordionItem secondMiddle = new MockAccordionItem(30, 10, 20, true);
        AccordionItem top = new MockAccordionItem(0, 10, 20, false);

        AccordionLayout.moveDown(20, 150, secondMiddle, asList(top, secondMiddle, firstMiddle, bottom));

        assertThat(bottom.getHeaderPos()).isEqualTo(110);
        assertThat(firstMiddle.getHeaderPos()).isEqualTo(80);
    }

    @Test
    void moveUp_givenSimpleMovableMiddle_thenShouldMoveMiddle()
    {
        AccordionItem bottom = new MockAccordionItem(90, 10, 20, true);
        AccordionItem middle = new MockAccordionItem(60, 10, 20, false);
        AccordionItem top = new MockAccordionItem(0, 10, 20, false);

        AccordionLayout.moveUp(20, bottom, asList(top, middle, bottom));

        assertThat(bottom.getHeaderPos()).isEqualTo(70);
        assertThat(middle.getHeaderPos()).isEqualTo(40);
    }

    @Test
    void moveUp_givenTwoMovableElement_thenShouldMoveBoth()
    {
        AccordionItem bottom = new MockAccordionItem(140, 10, 20, true);
        AccordionItem firstMiddle = new MockAccordionItem(110, 10, 20, false);
        AccordionItem secondMiddle = new MockAccordionItem(65, 10, 30, false);
        AccordionItem top = new MockAccordionItem(0, 10, 20, false);

        AccordionLayout.moveUp(20, bottom, asList(top, secondMiddle, firstMiddle, bottom));

        assertThat(bottom.getHeaderPos()).isEqualTo(120);
        assertThat(firstMiddle.getHeaderPos()).isEqualTo(90);
        assertThat(secondMiddle.getHeaderPos()).isEqualTo(50);
    }

    private static class MockAccordionItem implements AccordionItem
    {
        private float   headerPos;
        private float   headerSize;
        private float   minimalSize;
        private boolean isCollapsed;

        public MockAccordionItem(float headerPos, float headerSize, float minimalSize, boolean isCollapsed)
        {
            this.headerPos = headerPos;
            this.headerSize = headerSize;
            this.minimalSize = minimalSize;
            this.isCollapsed = isCollapsed;
        }

        @Override
        public float getHeaderPos()
        {
            return headerPos;
        }

        @Override
        public void setHeaderPos(float headerPos)
        {
            this.headerPos = headerPos;
        }

        @Override
        public float getHeaderSize()
        {
            return headerSize;
        }

        public void setHeaderSize(float headerSize)
        {
            this.headerSize = headerSize;
        }

        @Override
        public float getMinimalSize()
        {
            return minimalSize;
        }

        public void setMinimalSize(float minimalSize)
        {
            this.minimalSize = minimalSize;
        }

        @Override
        public boolean isCollapsed()
        {
            return isCollapsed;
        }

        public void setCollapsed(boolean collapsed)
        {
            isCollapsed = collapsed;
        }
    }
}