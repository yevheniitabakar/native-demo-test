package com.demo.framework.annotations;

import java.lang.annotation.*;

/**
 * Annotation for marking test methods that should be retried on failure
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Retry {
    /**
     * Number of retry attempts
     */
    int count() default 2;
}

