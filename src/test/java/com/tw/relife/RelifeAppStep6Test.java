package com.tw.relife;

import com.tw.relife.controller.OneActionController;
import com.tw.relife.controller.OneDifferentActionController;
import com.tw.relife.controller.OneSameAndOneDiffActionsController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class RelifeAppStep6Test {
    @Test
    void should_tell_part_different_in_all_controller() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(OneActionController.class)
                .addController(OneDifferentActionController.class)
                .build();

        RelifeApp app = new RelifeApp(handler);

        RelifeRequest request1 = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeRequest request2 = new RelifeRequest("/path/bye", RelifeMethod.GET);

        RelifeResponse response1 = app.process(request1);
        RelifeResponse response2 = app.process(request2);

        assertEquals(200, response1.getStatus());
        assertEquals(202, response2.getStatus());
    }

    @Test
    void should_get_first_response_when_different_controller_has_same_reflect_action() {
        RelifeAppHandler handler = new RelifeMvcHandlerBuilder()
                .addController(OneSameAndOneDiffActionsController.class)
                .addController(OneActionController.class)
                .build();

        RelifeApp app = new RelifeApp(handler);
        RelifeRequest request1 = new RelifeRequest("/path", RelifeMethod.GET);
        RelifeResponse response1 = app.process(request1);
        RelifeResponse response2 = app.process(request1);

        assertEquals(201, response1.getStatus());
        assertEquals(201, response2.getStatus());
    }

    @Test
    void should_throw_illegal_exception_when_add_same_controller() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RelifeMvcHandlerBuilder()
                    .addController(OneSameAndOneDiffActionsController.class)
                    .addController(OneSameAndOneDiffActionsController.class)
                    .build();
        });
    }
}
