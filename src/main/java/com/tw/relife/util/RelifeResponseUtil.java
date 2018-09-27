package com.tw.relife.util;

import com.tw.relife.RelifeRequest;
import com.tw.relife.RelifeResponse;
import com.tw.relife.annonation.RelifeStatusCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.tw.relife.RelifeApp.STATUS_CODE_CLASS;

public class RelifeResponseUtil {
    public static RelifeResponse getResponseFromController(Class<?> controllerClass, Method method, RelifeRequest request) {
        RelifeResponse response;
        try {
            boolean accessible = method.isAccessible();
            method.setAccessible(true);
            response = (RelifeResponse) method.invoke(controllerClass.newInstance(), request);
            method.setAccessible(accessible);
            return response;
        } catch (IllegalAccessException | InstantiationException e) {
            return new RelifeResponse(500);
        } catch (InvocationTargetException e) {
            return getResponseFromException(e);
        }
    }

    public static RelifeResponse getResponseFromException(Exception e) {
        Throwable cause = e.getCause();
        RelifeStatusCode declaredAnnotation = cause.getClass().getDeclaredAnnotation(STATUS_CODE_CLASS);
        return declaredAnnotation == null ? new RelifeResponse(500) : new RelifeResponse(declaredAnnotation.value());
    }
}
