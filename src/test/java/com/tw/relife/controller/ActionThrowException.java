package com.tw.relife.controller;

import com.tw.relife.RelifeMethod;
import com.tw.relife.RelifeRequest;
import com.tw.relife.RelifeResponse;
import com.tw.relife.annonation.RelifeController;
import com.tw.relife.annonation.RelifeRequestMapping;
import com.tw.relife.exception.MethodNotAllowedException;

@RelifeController
public class ActionThrowException {

    @RelifeRequestMapping(value = "/path", method = RelifeMethod.GET)
    public RelifeResponse sayHello(RelifeRequest request) {
        throw new MethodNotAllowedException();
    }

    @RelifeRequestMapping(value = "/path/without", method = RelifeMethod.GET)
    public RelifeResponse sayHelloToo(RelifeRequest request) {
        throw new IllegalArgumentException();
    }
}
