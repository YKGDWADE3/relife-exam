package com.tw.relife.controller;

import com.tw.relife.RelifeMethod;
import com.tw.relife.RelifeRequest;
import com.tw.relife.RelifeResponse;
import com.tw.relife.annonation.RelifeController;
import com.tw.relife.annonation.RelifeRequestMapping;

@RelifeController
public class OneSameAndOneDiffActionsController {

    @RelifeRequestMapping(value = "/path", method = RelifeMethod.GET)
    public RelifeResponse sayHello(RelifeRequest request) {
        return new RelifeResponse(
                201,
                "Hello from " + request.getPath(),
                "text/plain");
    }

    @RelifeRequestMapping(value = "/path/bye", method = RelifeMethod.GET)
    public RelifeResponse sayBye(RelifeRequest request) {
        return new RelifeResponse(
                202,
                "Hello from " + request.getPath(),
                "text/plain");
    }


}
