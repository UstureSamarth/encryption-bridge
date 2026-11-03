package com.bridge.workflow;

import com.bridge.workflow.contract.WorkflowCommitParticipant;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class WorkflowExecution {

    private final long flowStart;
    private final StringBuilder metrics;
    private final List<WorkflowCommitParticipant> commitParticipants;

    public WorkflowExecution() {
        this.flowStart = System.nanoTime();
        this.metrics = new StringBuilder();
        this.commitParticipants = new ArrayList<>();
    }
}
