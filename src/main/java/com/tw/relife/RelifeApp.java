package com.tw.relife;

public class RelifeApp implements RelifeAppHandler {
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
        RelifeResponse response = new RelifeResponse(200);
        try {
            response = handler.process(request);
        }catch (Exception ex){
            response = new RelifeResponse(500);
        }finally {
            return response;
        }

    }
}
