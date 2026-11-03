package com.bridge.workflow.test.select;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.contract.WorkflowParticipantSupport;

public class T3 implements WorkflowParticipantSupport {

    @Override
    public void doPrepare(WorkflowContext ctx) throws Exception {
        ctx.log(getClass().getName());
    }
}
