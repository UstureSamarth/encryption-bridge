package com.bridge.workflow.test.select;

import com.bridge.workflow.WorkflowContext;
import com.bridge.workflow.constants.WorkflowSelector;

import java.util.List;

public class Selector implements WorkflowSelector {

    @Override
    public List<String> select(WorkflowContext ctx) {
        return List.of("f2", "f3");
    }
}
