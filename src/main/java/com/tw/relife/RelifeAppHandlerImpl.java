package com.tw.relife;

import com.tw.relife.annonation.RelifeController;
import com.tw.relife.annonation.RelifeRequestMapping;
import com.tw.relife.annonation.RelifeStatusCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static com.tw.relife.RelifeApp.STATUS_CODE_CLASS;

public class RelifeAppHandlerImpl implements RelifeAppHandler{
    public static final Class<RelifeController> RELIFE_CONTROLLER_CLASS = RelifeController.class;
    public static final Class<RelifeRequestMapping> REQUEST_MAPPING_CLASS = RelifeRequestMapping.class;
    public static final int ACTION_PARAMETER_MAX = 1;
    public static final Class<RelifeRequest> REQUEST_CLASS = RelifeRequest.class;


    private final Map<RelifeAction, RelifeAppHandler> relifeActions = new HashMap<>();
    private final List<Class<?>> controllerClasses = new ArrayList<>();

    public RelifeResponse process(RelifeRequest request) {
        RelifeAction requestAction = new RelifeAction(request.getPath(), request.getMethod());
        if (relifeActions.containsKey(requestAction)) {
            RelifeResponse response = relifeActions.get(requestAction).process(request);
            return response == null ? new RelifeResponse(200) : response;
        }
        return new RelifeResponse(404);
    }

    public void addAction(RelifeAction action, RelifeAppHandler relifeAppHandler) {
        if (!relifeActions.containsKey(action)) {
            relifeActions.put(action, relifeAppHandler);
        }
    }

    public void addController(Class<?> controllerClass) {
        checkModifierAndAnnotation(controllerClass);
        checkActionParams(controllerClass);
        parseControllerAction(controllerClass);
        controllerClasses.add(controllerClass);
    }

    private void parseControllerAction(Class<?> controllerClass) {
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            RelifeRequestMapping annotation = method.getDeclaredAnnotation(REQUEST_MAPPING_CLASS);
            if (annotation != null) {
                RelifeAction action = new RelifeAction(annotation.value(), annotation.method());
                if (!relifeActions.containsKey(action)) {
                    relifeActions.put(action, request -> {
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
                            Throwable cause = e.getCause();
                            RelifeStatusCode declaredAnnotation = cause.getClass().getDeclaredAnnotation(STATUS_CODE_CLASS);
                            response = declaredAnnotation == null ? new RelifeResponse(500) : new RelifeResponse(declaredAnnotation.value());
                            return response;
                        }
                    });
                }
            }

        }
    }

    private void checkActionParams(Class<?> controllerClass) {
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getDeclaredAnnotation(REQUEST_MAPPING_CLASS) != null
                    && (method.getParameterCount() > ACTION_PARAMETER_MAX
                        || !Arrays.asList(method.getParameterTypes()).contains(REQUEST_CLASS)
                        )
            ) {
                throw new IllegalArgumentException();
            }

        }
    }

    private void checkModifierAndAnnotation(Class<?> controllerClass) {
        if (controllerClass == null) {
            throw new IllegalArgumentException();
        }
        int modifiers = controllerClass.getModifiers();
        if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers) || controllerClass.getDeclaredAnnotation(RELIFE_CONTROLLER_CLASS) == null) {
            throw new IllegalArgumentException();
        }
    }
}
