package com.bridge.workflow;

import com.bridge.workflow.constants.ParticipantResult;
import com.bridge.workflow.contract.WorkflowAbortParticipant;
import com.bridge.workflow.contract.WorkflowCommitParticipant;
import com.bridge.workflow.contract.WorkflowParticipant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@SuppressWarnings("unused")
public class WorkflowExecutor {

    private final WorkflowRegistry workflowRegistry;

    public WorkflowExecutor(WorkflowRegistry workflowRegistry) {
        this.workflowRegistry = workflowRegistry;
    }

    public void execute(String workflowName, WorkflowContext ctx) {
        Workflow workflow = getWorkflow(workflowName);
        long flowStart = System.nanoTime();
        StringBuilder metrics = new StringBuilder(256);
        metrics.append("\n\nWorkflow : ").append(workflowName).append("\n\n");
        List<WorkflowParticipant> participants = workflow.getParticipants();
        List<WorkflowCommitParticipant> commitParticipants = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            WorkflowParticipant participant = participants.get(i);
            long participantStart = System.nanoTime();
            try {
                ParticipantResult result = participant.prepare(ctx);
                long timeTaken = getTimeTaken(participantStart);
                metrics.append(String.format("%-60s | %-5s | %6d ms%n", participant.getClass().getCanonicalName(), result, timeTaken));
                if (result == ParticipantResult.ABORTED) {
                    executeAbortParticipants(participants, i, ctx, metrics);
                    appendFinalLogs(flowStart, metrics, ctx);
                    printFinalLogs(metrics.toString());
                    return;
                }
                if (participant instanceof WorkflowCommitParticipant commitParticipant) {
                    commitParticipants.add(commitParticipant);
                }
            } catch (Throwable e) {
                long timeTaken = getTimeTaken(participantStart);
                metrics.append(String.format("%-60s | %-8s | %6d ms%n", participant.getClass().getCanonicalName(),
                                "ABORTED", timeTaken))
                        .append("Exception : ")
                        .append(e.getClass().getSimpleName())
                        .append(" - ")
                        .append(e.getMessage())
                        .append('\n');
                executeAbortParticipants(participants, i, ctx, metrics);
                appendFinalLogs(flowStart, metrics, ctx);
                printFinalLogs(metrics.toString());
                printException(e);
                return;
            }
        }
        executeCommitParticipants(commitParticipants, ctx, metrics);
        appendFinalLogs(flowStart, metrics, ctx);
        printFinalLogs(metrics.toString());
    }

    private Workflow getWorkflow(String workflowName) {
        Workflow workflow = workflowRegistry.get(workflowName);
        if (workflow == null) {
            throw new IllegalArgumentException("Workflow not found : " + workflowName);
        }
        return workflow;
    }

    private void executeAbortParticipants(List<WorkflowParticipant> participants, int from, WorkflowContext ctx, StringBuilder metrics) {
        metrics.append("\n----- Abort Participants -----\n");
        for (int i = from; i < participants.size(); i++) {
            WorkflowParticipant participant = participants.get(i);
            try {
                if (participant instanceof WorkflowAbortParticipant abortParticipant) {
                    long start = System.nanoTime();
                    abortParticipant.abort(ctx);
                    appendParticipantLogs(participant.getClass(), metrics, start);
                }
            } catch (Throwable e) {
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

    @Async("virtualExecutor")
    public void executeAsync(String workflowName, WorkflowContext ctx) {
        execute(workflowName, ctx);
    }
}