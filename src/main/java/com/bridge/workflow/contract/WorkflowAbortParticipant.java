package com.bridge.workflow.contract;

import com.bridge.workflow.WorkflowContext;

@SuppressWarnings("all")
public interface WorkflowAbortParticipant {

    void abort(WorkflowContext ctx) throws Exception;
}
