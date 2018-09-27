package com.tw.relife.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.relife.RelifeRequest;
import com.tw.relife.RelifeResponse;
import com.tw.relife.annonation.RelifeStatusCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.tw.relife.RelifeApp.STATUS_CODE_CLASS;

public class RelifeResponseUtil {
    public static RelifeResponse getResponseFromController(Class<?> controllerClass, Method method, RelifeRequest request) {
        try {
            boolean accessible = method.isAccessible();
            method.setAccessible(true);
            Object object = method.invoke(controllerClass.newInstance(), request);
            method.setAccessible(accessible);
            return getResponseFromObject(object);
        } catch (IllegalAccessException | InstantiationException e) {
            return new RelifeResponse(500);
        } catch (InvocationTargetException e) {
            return getResponseFromException(e);
        }
    }

    private static RelifeResponse getResponseFromObject(Object object) {
        if (object == null) {
            return new RelifeResponse(200);
        }

        if (object.getClass().equals(RelifeResponse.class)) {
            return (RelifeResponse) object;
        }

        String string = null;
        try {
            string = new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new RelifeResponse(200, string, "application/json");
    }

    public static RelifeResponse getResponseFromException(Exception e) {
        Throwable cause = e.getCause();
        RelifeStatusCode declaredAnnotation = cause.getClass().getDeclaredAnnotation(STATUS_CODE_CLASS);
        return declaredAnnotation == null ? new RelifeResponse(500) : new RelifeResponse(declaredAnnotation.value());
    }
}
