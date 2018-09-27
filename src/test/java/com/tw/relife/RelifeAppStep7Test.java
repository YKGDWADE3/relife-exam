package com.tw.relife;

import com.tw.relife.controller.ObjectActionController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RelifeAppStep7Test {
    @Test
    void should_get_response_with_json_content() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(ObjectActionController.class)
                .build();

        RelifeApp app = new RelifeApp(handler);
        RelifeRequest request = new RelifeRequest("/path", RelifeMethod.POST);
        RelifeResponse response = app.process(request);

        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
    }
}
