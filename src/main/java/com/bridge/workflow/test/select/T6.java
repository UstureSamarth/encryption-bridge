package com.bridge.workflow.test.select;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.contract.WorkflowAbortParticipant;
import com.bridge.workflow.contract.WorkflowParticipantSupport;

public class T6 implements WorkflowParticipantSupport, WorkflowAbortParticipant {

    @Override
    public void doPrepare(WorkflowContext ctx) throws Exception {
        ctx.log(getClass().getName());
    }

    @Override
    public void abort(WorkflowContext ctx) throws InterruptedException {
        Thread.sleep(2000);
        ctx.log("Aborted in " + getClass().getName());
    }
}
