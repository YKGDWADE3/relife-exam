package com.tw.relife;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.relife.annonation.RelifeController;
import com.tw.relife.annonation.RelifeRequestMapping;
import com.tw.relife.util.RelifeResponseUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class RelifeAppHandlerImpl implements RelifeAppHandler{
    public static final Class<RelifeController> RELIFE_CONTROLLER_CLASS = RelifeController.class;
    public static final Class<RelifeRequestMapping> REQUEST_MAPPING_CLASS = RelifeRequestMapping.class;
    public static final int ACTION_PARAMETER_MAX = 1;
    public static final Class<RelifeRequest> REQUEST_CLASS = RelifeRequest.class;


    private final Map<RelifeAction, RelifeAppHandler> relifeActions = new HashMap<>();
    private final Set<Class<?>> controllerClasses = new HashSet<>();

    public RelifeResponse process(RelifeRequest request) {
        RelifeAction requestAction = new RelifeAction(request.getPath(), request.getMethod());
        if (relifeActions.containsKey(requestAction)) {
            Object object = relifeActions.get(requestAction).process(request);
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
        if (controllerClasses.contains(controllerClass)) {
            throw new IllegalArgumentException();
        }
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
                    relifeActions.put(action, request -> RelifeResponseUtil.getResponseFromController(controllerClass, method, request)
                    );
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
