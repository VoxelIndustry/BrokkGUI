package net.voxelindustry.brokkgui.component.impl;

import net.voxelindustry.brokkgui.event.LayoutEvent;
import net.voxelindustry.brokkgui.shape.Rectangle;
import net.voxelindustry.brokkgui.window.IGuiSubWindow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LayoutTransformTest
{
    @Test
    void givenParentWithAlreadySetWindow_andChildAdded_thenShouldFireAddEvents()
    {
        ArgumentCaptor<LayoutEvent.Add> layoutEventCaptor = ArgumentCaptor.forClass(LayoutEvent.Add.class);

        IGuiSubWindow window = mock(IGuiSubWindow.class);

        Rectangle parentElement = new Rectangle();
        parentElement.setWindow(window);

        Rectangle childElement = new Rectangle();
        parentElement.transform().addChild(childElement.transform());

        assertThat(childElement.getWindow()).isEqualTo(window);
        verify(window, times(2)).dispatchEvent(eq(LayoutEvent.ADD), layoutEventCaptor.capture());

        assertThat(layoutEventCaptor.getAllValues().get(0).getSource()).isEqualTo(parentElement);
        assertThat(layoutEventCaptor.getAllValues().get(1).getSource()).isEqualTo(childElement);

        verifyNoMoreInteractions(window);
    }

    @Test
    void givenParentWithAlreadySetWindow_andChildRemoved_thenShouldFireRemoveEvent()
    {
        ArgumentCaptor<LayoutEvent.Remove> layoutEventCaptor = ArgumentCaptor.forClass(LayoutEvent.Remove.class);

        IGuiSubWindow window = mock(IGuiSubWindow.class);

        Rectangle parentElement = new Rectangle();

        Rectangle childElement = new Rectangle();
        parentElement.transform().addChild(childElement.transform());

        parentElement.setWindow(window);

        parentElement.transform().removeChild(childElement.transform());

        assertThat(childElement.getWindow()).isNull();

        verify(window, times(2)).dispatchEvent(eq(LayoutEvent.ADD), any(LayoutEvent.Add.class));
        verify(window).dispatchEvent(eq(LayoutEvent.REMOVE), layoutEventCaptor.capture());

        assertThat(layoutEventCaptor.getValue().getSource()).isEqualTo(childElement);

        verifyNoMoreInteractions(window);
    }

    @Test
    void givenElementWithWindow_removeWindow_thenShouldFireRemoveEvent()
    {
        ArgumentCaptor<LayoutEvent.Remove> layoutEventCaptor = ArgumentCaptor.forClass(LayoutEvent.Remove.class);

        IGuiSubWindow window = mock(IGuiSubWindow.class);

        Rectangle parentElement = new Rectangle();

        parentElement.setWindow(window);
        parentElement.setWindow(null);


        assertThat(parentElement.getWindow()).isNull();
        verify(window).dispatchEvent(eq(LayoutEvent.ADD), any(LayoutEvent.Add.class));
        verify(window).dispatchEvent(eq(LayoutEvent.REMOVE), layoutEventCaptor.capture());
        assertThat(layoutEventCaptor.getValue().getSource()).isEqualTo(parentElement);

        verifyNoMoreInteractions(window);
    }
}