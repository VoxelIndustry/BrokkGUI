package net.voxelindustry.brokkgui.element.layout;

import net.voxelindustry.brokkgui.element.MockGuiRenderer;
import net.voxelindustry.brokkgui.style.adapter.StyleEngine;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class BrokkGuiTestExtension implements ParameterResolver, BeforeAllCallback
{
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException
    {
        return parameterContext.getParameter().getType().equals(MockGuiRenderer.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException
    {
        return new MockGuiRenderer();
    }

    @Override
    public void beforeAll(ExtensionContext context)
    {
        context.getTestClass().ifPresent(clazz ->
        {
            if (clazz.getAnnotation(BrokkGuiTest.class).styleEngine())
            {
                StyleEngine.getInstance().start();
            }
        });
    }
}
