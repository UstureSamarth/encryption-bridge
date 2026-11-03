package com.bridge.workflow.exception;

import lombok.Getter;

@SuppressWarnings("unused")
public final class WorkflowException extends Exception {

    @Getter
    String detail;

    public WorkflowException(String s) {
        super(s);
    }

    public WorkflowException(String s, String detail) {
        super(s);
        this.detail = detail;
    }
}
