package com.bridge.workflow.contract;

import com.bridge.workflow.constants.ParticipantResult;
import com.bridge.workflow.WorkflowContext;

@SuppressWarnings("all")
public interface WorkflowParticipant {

    ParticipantResult prepare(WorkflowContext context) throws Exception;

}
