package io.github.vialdevelopment.attendance.attender;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to indicate which methods should be attended to
 *
 * @author cats
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attend {
    /**
     * This is the priority of the {@link Attender}
     * @return the priority
     */
    long value() default 0;
}
