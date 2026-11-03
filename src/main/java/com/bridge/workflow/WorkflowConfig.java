package com.bridge.workflow;

import com.bridge.workflow.test.*;
import com.bridge.workflow.test.select.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class WorkflowConfig {

    private final WorkflowRegistry workflowRegistry;

    public WorkflowConfig(WorkflowRegistry workflowRegistry) {
        this.workflowRegistry = workflowRegistry;
    }

    @PostConstruct
    public void init() {
        workflowRegistry.register("test")
                .addParticipant(TestParticipant.class)
                .addParticipant(TestParticipant2.class)
                .addParticipant(TestParticipant3.class)
                .addParticipant(TestParticipant4.class);

        workflowRegistry.register("test2")
                .addFlow("test")
                .addParticipant(TestParticipant5.class);

        workflowRegistry.register("test3")
                .addParticipant(TestParticipant.class);

        workflowRegistry.register("f1")
                .addParticipant(T1.class)
                .addParticipant(Selector.class)
                .addParticipant(T6.class);

        workflowRegistry.register("f2")
                .addParticipant(T2.class)
                .addParticipant(T3.class);

        workflowRegistry.register("f3")
                .addParticipant(T4.class)
                .addParticipant(T5.class);

    }
}
