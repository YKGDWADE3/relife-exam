package com.tw.relife;

import java.util.ArrayList;
import java.util.List;

public class RelifeAppHandlerImpl implements RelifeAppHandler{
    private final List<RelifeAction> relifeActions = new ArrayList<>();

    public RelifeResponse process(RelifeRequest request) {
        RelifeAction requestAction = new RelifeAction(request.getPath(), request.getMethod());
        for (RelifeAction action : relifeActions) {
            if (action.equals(requestAction)) {
                RelifeResponse response = action.getRelifeAppHandler().process(request);
                return response == null ? new RelifeResponse(200) : response;
            }
        }
        return new RelifeResponse(404);
    }

    public void addAction(RelifeAction action) {
        relifeActions.add(action);
    }

}
