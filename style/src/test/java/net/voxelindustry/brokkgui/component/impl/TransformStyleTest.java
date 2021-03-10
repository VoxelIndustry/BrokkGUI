package net.voxelindustry.brokkgui.component.impl;

import net.voxelindustry.brokkgui.component.RequiredOverride;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.fail;

class TransformStyleTest
{
    static final Logger logger = LoggerFactory.getLogger(TransformStyleTest.class);

    @Test
    void inspectClass_givenClass_thenShouldOverrideRequiredMethods()
    {
        Class<TransformStyle> transformStyleClass = TransformStyle.class;
        Class<Transform> transformClass = Transform.class;

        List<Method> requiredMethods = stream(transformClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequiredOverride.class))
                .collect(toList());

        List<Method> missingMethods = requiredMethods.stream().filter(method ->
        {
            Method override = null;
            try
            {
                override = transformStyleClass.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException ignored)
            {
            }

            return override == null || !override.getDeclaringClass().equals(transformStyleClass);
        }).collect(toList());

        if (missingMethods.isEmpty())
        {
            logger.info(() -> "Success. All " + requiredMethods.size() + " methods are overridden.");
            return;
        }

        fail("TransformStyle must override all required methods.\nMissing methods:\n" + missingMethods.stream().map(method ->
        {
            String parameters = method.getParameterTypes().length == 0 ? "void" :
                                stream(method.getParameterTypes()).map(Class::toString).collect(joining(","));
            return method.getName() + "(" + parameters + ")";
        }).collect(Collectors.joining("\n")));
    }
}