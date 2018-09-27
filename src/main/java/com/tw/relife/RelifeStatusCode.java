package com.tw.relife;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RelifeStatusCode {

    int value() default 200;
}
