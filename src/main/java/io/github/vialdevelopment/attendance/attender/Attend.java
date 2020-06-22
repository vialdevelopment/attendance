package io.github.vialdevelopment.attendance.attender;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author cats
 * @since June 21, 2020
 *
 * This is the annotation to note an event
 * I don't think it's really needed, but I it
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Attend {
    /**
     * @return the priority of the event
     */
    long priority() default 0;
}
