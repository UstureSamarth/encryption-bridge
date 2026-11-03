package com.bridge.workflow.test;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.contract.WorkflowAbortParticipant;
import com.bridge.workflow.contract.WorkflowParticipantSupport;

public class TestParticipant4 implements WorkflowParticipantSupport, WorkflowAbortParticipant {

    @Override
    public void doPrepare(WorkflowContext ctx) {
        ctx.log("in TestParticipant4");
    }

    @Override
    public void abort(WorkflowContext ctx) {
        ctx.log("Aborted is TestParticipant4");
    }
}
