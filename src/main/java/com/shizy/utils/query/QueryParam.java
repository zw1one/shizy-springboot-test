package com.shizy.utils.query;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)//供反射时读取，java默认runtime忽略注解
public @interface QueryParam {

    /**
     * column like param
     */
    boolean like() default false;

    /**
     * column = param
     */
    boolean eq() default false;

    /**
     * column between betweenStart and betweenEnd
     */
    boolean betweenStart() default false;

    /**
     * column between betweenStart and betweenEnd
     */
    boolean betweenEnd() default false;

    /**
     * column between betweenStart and betweenEnd
     */
    String column() default "";
}
