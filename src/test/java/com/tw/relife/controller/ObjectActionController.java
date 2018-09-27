package com.tw.relife.controller;

import com.tw.relife.PojoReturnValue;
import com.tw.relife.RelifeMethod;
import com.tw.relife.RelifeRequest;
import com.tw.relife.annonation.RelifeController;
import com.tw.relife.annonation.RelifeRequestMapping;

@RelifeController
public class ObjectActionController {
    @RelifeRequestMapping(value = "/path", method = RelifeMethod.POST)
    PojoReturnValue get(RelifeRequest request){
        return new PojoReturnValue().setName("YKG");
    }
}
