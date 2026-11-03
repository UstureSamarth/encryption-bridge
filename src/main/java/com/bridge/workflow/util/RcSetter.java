package com.bridge.workflow.util;

import com.bridge.workflow.WorkflowContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.bridge.workflow.constants.WorkflowConstants.RC;
import static com.bridge.workflow.constants.WorkflowConstants.RC_MSG;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RcSetter {

    public static void setResultCode(WorkflowContext ctx, String rc) {
        setResultCode(ctx, rc, null);
    }

    public static void setResultCode(WorkflowContext ctx, String rc, String detail) {
        ctx.put(RC, rc);
        if (detail != null)
            ctx.put(RC_MSG, detail);
    }
}
