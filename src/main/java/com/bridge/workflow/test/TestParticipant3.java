package com.bridge.workflow.test;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.contract.WorkflowCommitParticipant;
import com.bridge.workflow.contract.WorkflowParticipantSupport;

public class TestParticipant3 implements WorkflowParticipantSupport, WorkflowCommitParticipant {

    @Override
    public void doPrepare(WorkflowContext ctx) {
        ctx.log("name is TestParticipant3");
    }

    @Override
    public void commit(WorkflowContext ctx) throws InterruptedException {
        Thread.sleep(1000);
        ctx.log("Inside commit of TestParticipant3");
    }
}
