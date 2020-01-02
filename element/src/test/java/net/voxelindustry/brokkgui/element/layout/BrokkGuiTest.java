package net.voxelindustry.brokkgui.element.layout;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ExtendWith(BrokkGuiTestExtension.class)
public @interface BrokkGuiTest
{
    boolean styleEngine() default false;
}
