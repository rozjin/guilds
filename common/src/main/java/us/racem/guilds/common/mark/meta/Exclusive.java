package us.racem.guilds.common.mark.meta;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Exclusive {
    Class<?>[] with();
}
