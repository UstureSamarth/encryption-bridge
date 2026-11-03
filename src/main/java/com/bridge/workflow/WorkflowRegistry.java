package com.bridge.workflow;

import com.bridge.workflow.contract.WorkflowParticipant;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
@Component
public class WorkflowRegistry {

    private final Map<String, Workflow> workflows;
    private final AutowireCapableBeanFactory beanFactory;
    private final Map<String, WorkflowParticipant> workflowParticipantMap;

    public WorkflowRegistry(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.workflows = new ConcurrentHashMap<>();
        this.workflowParticipantMap = new ConcurrentHashMap<>();
    }

    public Workflow register(String workflowName) {
        if (workflowName == null || workflowName.isBlank()) {
            throw new IllegalArgumentException("Workflow name cannot be null or empty");
        }
        Workflow workflow = new Workflow(this);
        workflows.put(workflowName, workflow);
        return workflow;
    }

    public Workflow get(String workflowName) {
        return workflows.get(workflowName);
    }

    public WorkflowParticipant getParticipant(Class<? extends WorkflowParticipant> clazz) {
        return workflowParticipantMap.computeIfAbsent(clazz.getCanonicalName(), k -> beanFactory.createBean(clazz));
    }
}
