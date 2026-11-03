package com.bridge.workflow.test;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.contract.WorkflowParticipantSupport;

public class TestParticipant2 implements WorkflowParticipantSupport {

    @Override
    public void doPrepare(WorkflowContext ctx) {
        ctx.log("name is TestParticipant2");
    }
}
