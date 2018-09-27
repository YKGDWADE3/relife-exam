package com.tw.relife.controller;

import com.tw.relife.RelifeMethod;
import com.tw.relife.RelifeResponse;
import com.tw.relife.annonation.RelifeController;
import com.tw.relife.annonation.RelifeRequestMapping;

@RelifeController
public class ActionParamTypeInvalidController {
    @RelifeRequestMapping(value = "/path", method = RelifeMethod.GET)
    public RelifeResponse sayHello(String request) {
        return new RelifeResponse(
                200,
                "Hello from " + request,
                "text/plain");
    }
}
