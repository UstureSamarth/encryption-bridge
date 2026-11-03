package com.bridge.workflow;

import com.bridge.workflow.contract.WorkflowParticipant;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class Workflow {

    private final WorkflowRegistry workflowRegistry;
    @Getter
    private final List<WorkflowParticipant> participants;

    public Workflow(WorkflowRegistry workflowRegistry) {
        this.workflowRegistry = workflowRegistry;
        this.participants = new ArrayList<>();
    }

    public Workflow addParticipant(Class<? extends WorkflowParticipant> clazz) {
        WorkflowParticipant participant = workflowRegistry.getParticipant(clazz);
        participants.add(participant);
        return this;
    }

    public Workflow addFlow(String workflowName) {
        if (workflowName == null || workflowName.isBlank()) {
            throw new IllegalArgumentException("Workflow name cannot be null or empty");
        }
        Workflow workflow = workflowRegistry.get(workflowName);
        if (workflow == null) {
            throw new IllegalArgumentException("Workflow not found : " + workflowName);
        }
        participants.addAll(workflow.getParticipants());
        return this;
    }
}
