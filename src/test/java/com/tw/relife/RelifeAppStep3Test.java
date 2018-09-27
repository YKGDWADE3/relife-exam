package com.tw.relife;

import com.tw.relife.exception.MethodNotAllowedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RelifeAppStep3Test {
    @Test
    void should_get_status_code_when_path_and_method_all_match() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/path", RelifeMethod.GET,
                        request -> new RelifeResponse(200, "Hello", "text/plain")).build();
        RelifeApp app = new RelifeApp(handler);
        RelifeResponse response = app.process(
                new RelifeRequest("/path", RelifeMethod.GET));
        assertEquals(200, response.getStatus());
        assertEquals("Hello", response.getContent());
    }

    @Test
    void should_response_404_when_request_path_or_method_not_match() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/path",
                        RelifeMethod.GET,
                        request -> new RelifeResponse(200, "Hello", "text/plain")
                )
                .build();

        RelifeApp app = new RelifeApp(handler);

        RelifeResponse responsePathNotMatch = app.process(
                new RelifeRequest("/path/notMatch", RelifeMethod.GET));

        RelifeResponse responseMethodNotMatch = app.process(
                new RelifeRequest("/path", RelifeMethod.POST));


        assertEquals(404, responsePathNotMatch.getStatus());
        assertEquals(404, responseMethodNotMatch.getStatus());

    }

    @Test
    void should_throw_illegal_exception_when_any_of_add_action_arg_null() {
        assertThrows(IllegalArgumentException.class,
                () -> new RelifeMvcHandlerBuilder()
                        .addAction(
                                null,
                                RelifeMethod.GET,
                                request -> new RelifeResponse(200, "Hello", "text/plain")
                        )
        );

        assertThrows(IllegalArgumentException.class,
                () -> new RelifeMvcHandlerBuilder()
                        .addAction(
                                "/path",
                                null,
                                request -> new RelifeResponse(200, "Hello", "text/plain")
                        )
        );


        assertThrows(IllegalArgumentException.class,
                () -> new RelifeMvcHandlerBuilder()
                        .addAction(
                                "/path",
                                RelifeMethod.GET,
                                null));
    }

    @Test
    void should_response_200_when_add_action_handler_return_is_null() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/path",
                        RelifeMethod.GET,
                        request -> null).build();

        RelifeApp app = new RelifeApp(handler);
        RelifeResponse response = app.process(
                new RelifeRequest("/path", RelifeMethod.GET));

        assertEquals(200, response.getStatus());
        assertNull(response.getContent());
        assertNull(response.getContentType());
    }

    @Test
    void should_catch_unhandled_exception_as_internal_server_error() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/any/path",
                        RelifeMethod.GET,
                        request -> {
                            throw new RuntimeException("error occurred");
                        })
                .build();

        RelifeApp app = new RelifeApp(handler);
        RelifeRequest whateverRequest = new RelifeRequest("/any/path", RelifeMethod.GET);
        RelifeResponse response = app.process(whateverRequest);

        assertEquals(500, response.getStatus());
    }

    @Test
    void should_get_response_with_exact_status_code() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addAction(
                        "/any/path",
                        RelifeMethod.GET,
                        request -> {
                            throw new MethodNotAllowedException();
                        })
                .build();

        RelifeApp app = new RelifeApp(handler);
        RelifeRequest whateverRequest = new RelifeRequest("/any/path", RelifeMethod.GET);
        RelifeResponse response = app.process(whateverRequest);
        assertEquals(403, response.getStatus());
    }
}
