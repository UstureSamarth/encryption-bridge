package com.bridge.workflow.test;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.contract.WorkflowAbortParticipant;
import com.bridge.workflow.contract.WorkflowParticipantSupport;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class TestParticipant5 implements WorkflowParticipantSupport, WorkflowAbortParticipant {

    @Override
    public void doPrepare(WorkflowContext ctx) throws Exception {
        ctx.log("name is TestParticipant5");
        ctx.log(this.toString());
        Thread.sleep(1000);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Workflow completed");
        ctx.getFuture().complete(ResponseEntity.ok(response));
    }

    @Override
    public void abort(WorkflowContext ctx) throws Exception {
        doPrepare(ctx);
    }
}
