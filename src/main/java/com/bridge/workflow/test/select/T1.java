package com.bridge.workflow.test.select;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.contract.WorkflowCommitParticipant;
import com.bridge.workflow.contract.WorkflowParticipantSupport;

public class T1 implements WorkflowParticipantSupport, WorkflowCommitParticipant {

    @Override
    public void doPrepare(WorkflowContext ctx) throws Exception {
        ctx.log(getClass().getName());
    }

    @Override
    public void commit(WorkflowContext ctx) throws Exception {
        ctx.log("Commit of " + getClass().getName());
    }
}
