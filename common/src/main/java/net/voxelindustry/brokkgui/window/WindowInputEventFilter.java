package net.voxelindustry.brokkgui.window;

import java.util.function.BiFunction;

@FunctionalInterface
public interface WindowInputEventFilter extends BiFunction<IGuiWindow, InputType, WindowEventFilter>
{
}
