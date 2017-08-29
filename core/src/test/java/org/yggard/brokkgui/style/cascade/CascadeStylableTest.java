package org.yggard.brokkgui.style.cascade;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.yggard.brokkgui.control.GuiFather;
import org.yggard.brokkgui.paint.Color;
import org.yggard.brokkgui.style.ICascadeStylable;

@RunWith(MockitoJUnitRunner.class)
public class CascadeStylableTest
{

    GuiFather node1;
    GuiFather node2;
    GuiFather node3;

    @Before
    public void init()
    {
        // Init GUI Element
        this.node1 = new GuiFather();
        this.node2 = new GuiFather();
        this.node3 = new GuiFather();

        // Set GUI hierarchy
        this.node2.setFather(this.node1);
        this.node3.setFather(this.node2);

        // Init style
        this.node1.getStyle().registerProperty("-color", Color.WHITE, Color.class);
        this.node3.getStyle().registerProperty("-color", Color.RED, Color.class);

    }

    @Test
    public void testHierarchy()
    {
        ICascadeStylable excepted1 = this.node2;
        ICascadeStylable excepted2 = this.node1;
        ICascadeStylable excepted3 = this.node1;

        ICascadeStylable actual1 = this.node3.getParent();
        ICascadeStylable actual2 = this.node2.getParent();
        ICascadeStylable actual3 = this.node3.getParent().getParent();

        assertThat(excepted1).isEqualTo(actual1);
        assertThat(excepted2).isEqualTo(actual2);
        assertThat(excepted3).isEqualTo(actual3);
    }
}
