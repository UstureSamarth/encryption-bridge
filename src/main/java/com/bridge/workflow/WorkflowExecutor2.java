package com.bridge.workflow;

import com.bridge.workflow.constants.ParticipantResult;
import com.bridge.workflow.constants.WorkflowSelector;
import com.bridge.workflow.contract.WorkflowAbortParticipant;
import com.bridge.workflow.contract.WorkflowCommitParticipant;
import com.bridge.workflow.contract.WorkflowParticipant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@SuppressWarnings("unused")
public class WorkflowExecutor2 {

    private final WorkflowRegistry workflowRegistry;

    public WorkflowExecutor2(WorkflowRegistry workflowRegistry) {
        this.workflowRegistry = workflowRegistry;
    }

    @SuppressWarnings("all")
    public WorkflowExecutionResult execute(String workflowName, WorkflowContext ctx) {
        WorkflowExecution execution = new WorkflowExecution();
        execution.getMetrics().append("\n========== Workflow : ").append(workflowName).append(" ==========\n");
        WorkflowExecutionResult result = executeInternal(workflowName, ctx, execution);
        if (result != WorkflowExecutionResult.ABORTED) {
            executeCommitParticipants(execution.getCommitParticipants(), ctx, execution.getMetrics());
        }
        appendFinalLogs(execution.getFlowStart(), execution.getMetrics(), ctx);
        printFinalLogs(execution.getMetrics().toString());
        return result;
    }

    private WorkflowExecutionResult executeInternal(String workflowName, WorkflowContext ctx, WorkflowExecution execution) {
        Workflow workflow = getWorkflow(workflowName);
        List<WorkflowParticipant> participants = workflow.getParticipants();
        List<WorkflowCommitParticipant> commitParticipants = execution.getCommitParticipants();

        for (int i = 0; i < participants.size(); i++) {
            WorkflowParticipant participant = participants.get(i);
            long participantStart = System.nanoTime();
            try {
                ParticipantResult result = participant.prepare(ctx);
                long timeTaken = getTimeTaken(participantStart);
                execution.getMetrics().append(
                        String.format("%-60s | %-8s | %6d ms%n", participant.getClass().getCanonicalName(),
                                result,
                                timeTaken));

                if (result == ParticipantResult.ABORTED) {
                    executeAbortParticipants(participants, i, ctx, execution.getMetrics());
                    return WorkflowExecutionResult.ABORTED;
                }

                if (participant instanceof WorkflowCommitParticipant commitParticipant) {
                    commitParticipants.add(commitParticipant);
                }

                if (participant instanceof WorkflowSelector selector) {
                    WorkflowExecutionResult resultOfFlow = executeSelectedWorkflows(selector, participants, i, ctx, execution);
                    if (resultOfFlow == WorkflowExecutionResult.ABORTED) {
                        return resultOfFlow;
                    }
                }

            } catch (Exception e) {
                long timeTaken = getTimeTaken(participantStart);
                execution.getMetrics().append(String.format("%-60s | %-8s | %6d ms%n",
                                participant.getClass().getCanonicalName(), "ABORTED", timeTaken))
                        .append("Exception : ")
                        .append(e.getClass().getSimpleName())
                        .append(" - ")
                        .append(e.getMessage())
                        .append('\n');

                executeAbortParticipants(participants, i, ctx, execution.getMetrics());
                printException(e);
                return WorkflowExecutionResult.ABORTED;
            }
        }
        return WorkflowExecutionResult.SUCCESS;
    }

    private WorkflowExecutionResult executeSelectedWorkflows(WorkflowSelector selector,
                                                             List<WorkflowParticipant> participants,
                                                             int participantIndex, WorkflowContext ctx,
                                                             WorkflowExecution execution) {
        List<String> workflows = selector.select(ctx);

        if (workflows == null || workflows.isEmpty()) {
            return WorkflowExecutionResult.SUCCESS;
        }

        StringBuilder metrics = execution.getMetrics();

        boolean aborted = false;
        metrics.append("\n----- Selected Workflows -----\n");
        for (String workflowName : workflows) {
            if (!aborted) {
                metrics.append("Starting Workflow : ")
                        .append(workflowName)
                        .append('\n');
                WorkflowExecutionResult result = executeInternal(workflowName, ctx, execution);
                if (result == WorkflowExecutionResult.ABORTED) {
                    aborted = true;
                }
            } else {
                executeAbortWorkflow(workflowName, ctx, execution);
            }
        }

        if (aborted) {
            executeAbortParticipants(participants, participantIndex, ctx, metrics);
            return WorkflowExecutionResult.ABORTED;
        }

        return WorkflowExecutionResult.SUCCESS;
    }

    private Workflow getWorkflow(String workflowName) {
        Workflow workflow = workflowRegistry.get(workflowName);
        if (workflow == null) {
            throw new IllegalArgumentException("Workflow not found : " + workflowName);
        }
        return workflow;
    }

    private void executeAbortParticipants(List<WorkflowParticipant> participants, int from, WorkflowContext ctx, StringBuilder metrics) {
        boolean headerPrinted = false;
        for (int i = from; i < participants.size(); i++) {
            WorkflowParticipant participant = participants.get(i);
            if (!(participant instanceof WorkflowAbortParticipant abortParticipant)) {
                continue;
            }
            if (!headerPrinted) {
                metrics.append("\n----- Abort Participants -----\n");
                headerPrinted = true;
            }
            try {
                long start = System.nanoTime();
                abortParticipant.abort(ctx);
                appendParticipantLogs(participant.getClass(), metrics, start);
            } catch (Exception e) {
                metrics.append(participant.getClass().getSimpleName())
                        .append(" - ABORT_EXCEPTION\n");
                printException(e);
            }
        }
    }

    private void executeCommitParticipants(List<WorkflowCommitParticipant> participants, WorkflowContext ctx, StringBuilder metrics) {
        if (!participants.isEmpty()) {
            metrics.append("\n----- Commit Participants -----\n");
            for (WorkflowCommitParticipant participant : participants) {
                try {
                    long start = System.nanoTime();
                    participant.commit(ctx);
                    appendParticipantLogs(participant.getClass(), metrics, start);
                } catch (Throwable e) {
                    printException(e);
                }
            }
        }
    }

    private void appendParticipantLogs(Class<?> clazz, StringBuilder metrics, long start) {
        long timeTaken = getTimeTaken(start);
        metrics.append(clazz.getCanonicalName())
                .append(" - ")
                .append(timeTaken)
                .append(" ms\n");
    }

    private void appendFinalLogs(long flowStart, StringBuilder metrics, WorkflowContext ctx) {
        long totalTime = getTimeTaken(flowStart);
        metrics.append("\n")
                .append("Total Flow Time : ")
                .append(totalTime)
                .append(" ms\n")
                .append("\nContext Logs : \n")
                .append(ctx.getContextLogs())
                .append("\nContext Values : ")
                .append(ctx.getContextValues())
                .append("\n");
    }

    private void printFinalLogs(String msg) {
        log.info(msg);
    }

    private void printException(Throwable e) {
        log.error("Exception : ", e);
    }

    private long getTimeTaken(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }

    private void executeAbortWorkflow(String workflowName, WorkflowContext ctx, WorkflowExecution execution) {
        Workflow workflow = getWorkflow(workflowName);
        executeAbortParticipants(workflow.getParticipants(), 0, ctx, execution.getMetrics());
    }

    @Async("virtualExecutor")
    public void executeAsync(String workflowName, WorkflowContext ctx) {
//        execute(workflowName, ctx);
    }
}

/*
* for (String workflowName : workflows) {
            long start = System.nanoTime();
            metrics.append("Starting Workflow : ")
                    .append(workflowName)
                    .append('\n');
            WorkflowExecutionResult result = executeInternal(workflowName, ctx, execution);
            long timeTaken = getTimeTaken(start);
            metrics.append("Completed Workflow : ")
                    .append(workflowName)
                    .append(" | ")
                    .append(result)
                    .append(" | ")
                    .append(timeTaken)
                    .append(" ms\n");

            if (result == WorkflowExecutionResult.ABORTED) {
                metrics.append("Workflow Aborted : ")
                        .append(workflowName)
                        .append('\n');
                executeAbortParticipants(participants, participantIndex, ctx, metrics);
                return WorkflowExecutionResult.ABORTED;
            }
        }

        return WorkflowExecutionResult.SUCCESS;
        * */