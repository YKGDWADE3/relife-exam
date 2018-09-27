package com.tw.relife;

import com.tw.relife.annonation.RelifeStatusCode;
import com.tw.relife.util.RelifeResponseUtil;

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
            response = handler.process(request);
        }catch (RuntimeException ex){
            return RelifeResponseUtil.getResponseFromException(ex);
        }
        return response;

    }
}
