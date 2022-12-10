package com.neko233.sql.lightrail.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    String value();

    String jdbcType() default "";

    String comment() default "";

    boolean nullable() default true;

    /**
     * Position of this column in the table for this class (0=first, -1=unset).
     *
     * @return (relative) position of this column
     * @since 3.1
     */
    int position() default -1;

}
