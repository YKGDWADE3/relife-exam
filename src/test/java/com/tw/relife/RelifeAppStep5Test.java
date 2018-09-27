package com.tw.relife;

import com.tw.relife.controller.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RelifeAppStep5Test {
    @Test
    void should_get_response_from_controller_class() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(OneActionController.class)
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest request = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeResponse response = app.process(request);

        assertEquals(200, response.getStatus());
        assertEquals("Hello from /path", response.getContent());
    }

    @Test
    void should_throw_illegal_exception_when_controller_class_null_or_modifier_or_annotation_invalid() {
        assertThrows(IllegalArgumentException.class, () ->{
            new RelifeMvcHandlerBuilder()
                    .addController(AbstractController.class)
                    .build();
        });

        assertThrows(IllegalArgumentException.class, () ->{
            new RelifeMvcHandlerBuilder()
                    .addController(InterfaceController.class)
                    .build();
        });

        assertThrows(IllegalArgumentException.class, () ->{
            new RelifeMvcHandlerBuilder()
                    .addController(NoAnnotationController.class)
                    .build();
        });

        assertThrows(IllegalArgumentException.class, () ->{
            new RelifeMvcHandlerBuilder()
                    .addController(null)
                    .build();
        });
    }

    @Test
    void should_throw_illegal_exception_when_action_invalid() {
        assertThrows(IllegalArgumentException.class, () ->{
            new RelifeMvcHandlerBuilder()
                    .addController(ActionParamTypeInvalidController.class)
                    .build();
        });

        assertThrows(IllegalArgumentException.class, () ->{
            new RelifeMvcHandlerBuilder()
                    .addController(ActionTwoParamsController.class)
                    .build();
        });
    }

    @Test
    void should_get_same_response_from_controller_class_has_same_action() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(TwoSameActionsController.class)
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest request = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeResponse response1 = app.process(request);

        int status = response1.getStatus();
        String content = response1.getContent();


        RelifeResponse response2 = app.process(request);

        assertEquals(status, response2.getStatus());
        assertEquals(content, response2.getContent());
    }

    @Test
    void should_tell_apart_different_actions() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(ThreeDifferentActionsController.class)
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest requestHelloGet = new RelifeRequest("/path/hello", RelifeMethod.GET);
        RelifeRequest requestByeGet = new RelifeRequest("/path/bye", RelifeMethod.GET);
        RelifeRequest requestHelloPost = new RelifeRequest("/path/hello", RelifeMethod.POST);

        RelifeResponse responseHelloGet = app.process(requestHelloGet);
        RelifeResponse responseHelloPost = app.process(requestHelloPost);
        RelifeResponse responseByeGet = app.process(requestByeGet);

        assertEquals(200, responseHelloGet.getStatus());
        assertEquals(202, responseHelloPost.getStatus());
        assertEquals(201, responseByeGet.getStatus());


    }

    @Test
    void should_get_exact_response_when_action_throws_with_exception_annotation() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(ActionThrowException.class)
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest requestHelloGet = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeResponse response = app.process(requestHelloGet);
        assertEquals(403, response.getStatus());

    }

    @Test
    void should_get_500_response_when_action_throws_without_annotation() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(ActionThrowException.class)
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest requestHelloGet = new RelifeRequest("/path/without", RelifeMethod.GET);
        RelifeResponse response = app.process(requestHelloGet);
        assertEquals(500, response.getStatus());

    }

    @Test
    void should_get_first_response_when_use_controller_and_just_action() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(OneActionController.class)
                .addAction(
                        "/path",
                        RelifeMethod.GET,
                        request -> new RelifeResponse(201))
                .build();

        RelifeApp app = new RelifeApp(handler);
        RelifeRequest requestHelloGet = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeResponse response = app.process(requestHelloGet);
        assertEquals(200, response.getStatus());
    }

    @Test
    void should_get_200_response_when_controller_action_return_null() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(ActionNullController.class)
                .build();
        RelifeApp app = new RelifeApp(handler);
        RelifeRequest requestHelloGet = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeResponse response = app.process(requestHelloGet);
        assertEquals(200, response.getStatus());
    }
}
