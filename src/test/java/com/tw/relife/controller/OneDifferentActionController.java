package com.tw.relife.controller;

import com.tw.relife.RelifeMethod;
import com.tw.relife.RelifeRequest;
import com.tw.relife.RelifeResponse;
import com.tw.relife.annonation.RelifeController;
import com.tw.relife.annonation.RelifeRequestMapping;

@RelifeController
public class OneDifferentActionController {

    @RelifeRequestMapping(value = "/path/bye", method = RelifeMethod.GET)
    public RelifeResponse sayBye(RelifeRequest request) {
        return new RelifeResponse(
                202,
                "Hello from " + request.getPath(),
                "text/plain");
    }
}
