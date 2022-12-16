package com.neko233.sql.lightrail.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    String value();

    /**
     * 是否使用
     *
     * @return true 使用 / false 不参与任何 SQL 处理
     */
    boolean isUse() default true;

    String jdbcType() default "";

    String comment() default "";

    boolean nullable() default true;

}
