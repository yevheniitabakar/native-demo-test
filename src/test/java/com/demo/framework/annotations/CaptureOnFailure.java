package com.demo.framework.annotations;

import java.lang.annotation.*;

/**
 * Annotation for marking test methods that need screenshot on failure
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CaptureOnFailure {
    /**
     * Screenshot name prefix
     */
    String value() default "failure";
}

