package com.tw.relife;

public class RelifeMvcHandlerBuilder {

    private RelifeAppHandlerImpl relifeAppHandlerImpl;

    public RelifeMvcHandlerBuilder() {
        this.relifeAppHandlerImpl = new RelifeAppHandlerImpl();
    }

    public RelifeMvcHandlerBuilder addAction(String path, RelifeMethod method, RelifeAppHandler relifeAppHandler) {
        if (path == null || method == null || relifeAppHandler == null) {
            throw new IllegalArgumentException();
        }
        relifeAppHandlerImpl.addAction(new RelifeAction(path, method), relifeAppHandler);
        return this;
    }

    public RelifeMvcHandlerBuilder addController(Class<?> controllerClass) {
        relifeAppHandlerImpl.addController(controllerClass);
        return this;
    }

    public RelifeAppHandler build() {
        return relifeAppHandlerImpl;
    }


}
