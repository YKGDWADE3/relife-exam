package com.tw.relife;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RelifeAppStep4Test {
    @Test
    void should_response_depends_on_request_from_different_action() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/path",
                        RelifeMethod.GET,
                        request -> new RelifeResponse(200, "get action", "text/plain"))
                .addAction(
                        "/path",
                        RelifeMethod.POST,
                        request -> new RelifeResponse(403, "post action", "text/plain"))
                .build();

        RelifeApp app = new RelifeApp(handler);
        RelifeRequest requestGet = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeRequest requestPost = new RelifeRequest("/path", RelifeMethod.POST);

        RelifeResponse responseGet = app.process(requestGet);
        RelifeResponse responsePost = app.process(requestPost);

        assertEquals(200, responseGet.getStatus());
        assertEquals("get action", responseGet.getContent());

        assertEquals(403, responsePost.getStatus());
        assertEquals("post action", responsePost.getContent());
    }

    @Test
    void should_get_first_response_when_request_match_more_than_one_action() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/path",
                        RelifeMethod.POST,
                        request -> new RelifeResponse(200, "first action", "text/plain"))
                .addAction(
                    "/path",
                        RelifeMethod.POST,
                        request -> new RelifeResponse(403, "second action", "text/plain"))
                .build();

        RelifeApp app = new RelifeApp(handler);
        RelifeRequest requestPost = new RelifeRequest("/path", RelifeMethod.POST);

        RelifeResponse response = app.process(requestPost);

        assertEquals(200, response.getStatus());
        assertEquals("first action", response.getContent());
    }
}
