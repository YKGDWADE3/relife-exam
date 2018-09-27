package com.tw.relife.annonation;

import com.tw.relife.RelifeMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelifeRequestMapping {

    String value() default "";

    RelifeMethod method() default RelifeMethod.UNKNOWN;
}
