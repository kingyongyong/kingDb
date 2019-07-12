package com.sqy.kingdb.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface KingField {
    String value();
    boolean notNull() default false;
    boolean unique() default false;
    boolean primaryKey() default false;
}
