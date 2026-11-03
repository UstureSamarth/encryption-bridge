package com.bridge.workflow.constants;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.contract.WorkflowParticipant;

import java.util.List;

public interface WorkflowSelector extends WorkflowParticipant {

    @Override
    default ParticipantResult prepare(WorkflowContext context) {
        return ParticipantResult.PREPARED;
    }

    List<String> select(WorkflowContext ctx);
}
