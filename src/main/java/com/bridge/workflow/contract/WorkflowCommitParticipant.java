package com.bridge.workflow.contract;

import com.bridge.workflow.WorkflowContext;

@SuppressWarnings("all")
public interface WorkflowCommitParticipant {
    void commit(WorkflowContext ctx) throws Exception;
}
