package com.tw.relife;

public class RelifeApp implements RelifeAppHandler {
    public static final Class<RelifeStatusCode> STATUS_CODE_CLASS = RelifeStatusCode.class;
    private final RelifeAppHandler handler;

    public RelifeApp(RelifeAppHandler handler) {
        // TODO: You can start here
        if (handler == null) {
            throw new IllegalArgumentException();
        }
        this.handler = handler;
    }

    @Override
    public RelifeResponse process(RelifeRequest request) {
        RelifeResponse response;
        try {
            if (handler instanceof RelifeAppHandlerStatus) {
                if (!((RelifeAppHandlerStatus) handler).getAction().equals(new RelifeAction(request.getPath(), request.getMethod()))) {
                    return new RelifeResponse(404);
                }
            }
            response = handler.process(request);
            if (response == null) {
                response = new RelifeResponse(200);
            }
        }catch (RuntimeException ex){
            RelifeStatusCode declaredAnnotation = ex.getClass().getDeclaredAnnotation(STATUS_CODE_CLASS);
            response = declaredAnnotation == null ? new RelifeResponse(500) : new RelifeResponse(declaredAnnotation.value());
            return response;
        }
        return response;

    }
}
