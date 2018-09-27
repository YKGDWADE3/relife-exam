package com.tw.relife;

public class RelifeMvcHandlerBuilder {

    private String path;
    private RelifeMethod method;
    private RelifeAppHandler relifeAppHandler;

    public RelifeMvcHandlerBuilder() {

    }
    public RelifeMvcHandlerBuilder(String path, RelifeMethod method, RelifeAppHandler relifeAppHandler) {
        this.path = path;
        this.method = method;
        this.relifeAppHandler = relifeAppHandler;
    }

    public RelifeMvcHandlerBuilder addAction(String path, RelifeMethod method, RelifeAppHandler relifeAppHandler) {
        if (path == null || method == null || relifeAppHandler == null) {
            throw new IllegalArgumentException();
        }
        return new RelifeMvcHandlerBuilder(path, method, relifeAppHandler);
    }

    public RelifeAppHandler build() {
        return new RelifeAppHandlerStatus() {
            @Override
            public RelifeAction getAction() {
                return new RelifeAction(path, method);
            }

            @Override
            public RelifeResponse process(RelifeRequest request) {
                return relifeAppHandler.process(request);
            }
        };
    }


}
