package com.bridge.workflow.test;

import com.bridge.TestService;
import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.contract.WorkflowParticipantSupport;

public class TestParticipant implements WorkflowParticipantSupport {

    private final TestService testService;

    public TestParticipant(TestService testService) {
        this.testService = testService;
    }

    @Override
    public void doPrepare(WorkflowContext ctx) throws Exception {
        ctx.log(this.toString());
        ctx.put("name", "Samarth");
        ctx.log("name is samarth");
        ctx.log("Name from testservice is " + testService.getName());
        Thread.sleep(2000);
    }
}
