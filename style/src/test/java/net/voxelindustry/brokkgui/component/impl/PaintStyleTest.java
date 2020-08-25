package net.voxelindustry.brokkgui.component.impl;

import net.voxelindustry.brokkgui.component.RequiredOverride;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.fail;

class PaintStyleTest
{
    @Test
    void inspectClass_givenClass_thenShouldOverrideRequiredMethods()
    {
        Class<PaintStyle> paintStyleClass = PaintStyle.class;
        Class<Paint> paintClass = Paint.class;

        List<Method> requiredMethods = stream(paintClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequiredOverride.class))
                .collect(toList());

        List<Method> missingMethods = requiredMethods.stream().filter(method ->
        {
            Method override = null;
            try
            {
                override = paintStyleClass.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException ignored)
            {
            }

            return override == null || !override.getDeclaringClass().equals(paintStyleClass);
        }).collect(toList());

        if (missingMethods.isEmpty())
            return;
        
        fail("PaintStyle must override all required methods.\nMissing methods:\n" + missingMethods.stream().map(method ->
        {
            String parameters = method.getParameterTypes().length == 0 ? "void" :
                                stream(method.getParameterTypes()).map(Class::toString).collect(joining(","));
            return method.getName() + "(" + parameters + ")";
        }).collect(Collectors.joining("\n")));
    }
}