package com.bridge.workflow.contract;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.constants.ParticipantResult;
import com.bridge.workflow.exception.WorkflowException;

import static com.bridge.workflow.util.RcSetter.setResultCode;

@SuppressWarnings("all")
public interface WorkflowParticipantSupport extends WorkflowParticipant {

    default ParticipantResult prepare(WorkflowContext ctx) throws Exception {
        try {
            doPrepare(ctx);
            return ParticipantResult.PREPARED;
        } catch (WorkflowException we) {
            setResultCode(ctx, we.getMessage(), we.getDetail());
            throw we;
        } catch (Throwable t) {
            setResultCode(ctx, t.getMessage());
            throw t;
        }
    }

    void doPrepare(WorkflowContext ctx) throws Exception;
}
