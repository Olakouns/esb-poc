package sn.esmt.gesb.services.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import sn.esmt.gesb.dto.TPOWorkOrderDto;
import sn.esmt.gesb.dto.Workflow;
import sn.esmt.gesb.dto.WorkflowStep;
import sn.esmt.gesb.services.SagaOrchestratorService;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SagaOrchestratorServiceImpl implements SagaOrchestratorService {

    private final SoapClientService soapClientService;

    @Override
    public void executeSaga(List<WorkflowStep> workflowSteps, String callbackURL) {
        for (WorkflowStep workflowStep : workflowSteps) {
            try {
                // todo : send request here
                soapClientService.sendSoapRequest(workflowStep.getUrl(), workflowStep.getBodyContent());
            } catch (Exception exception) {
                this.rollback(workflowSteps, workflowSteps.indexOf(workflowStep), callbackURL);
                break;
            }
        }
    }

    private void rollback(List<WorkflowStep> workflowSteps, int index, String callbackURL) {
        if (index == -1) throw new RuntimeException("No rollback action found");
        if (index == 0) {
            log.error("Nothing to rollback");
        }
        List<WorkflowStep> actions = new LinkedList<>();
        for (int i = index - 1; i >= 0; i--) {
            if (workflowSteps.get(i).getFailureSteps() == null) continue;
            Collections.reverse(workflowSteps.get(i).getFailureSteps());
            actions.addAll(workflowSteps.get(i).getFailureSteps());
        }

        for (WorkflowStep action : actions) {
            executeFailure(action);
        }
        // TODO: 2/16/2024 send callback
    }

    @Retryable(retryFor = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public void executeFailure(WorkflowStep workflowStep) {
        soapClientService.sendSoapRequest(workflowStep.getUrl(), workflowStep.getBodyContent());
    }

    @Recover
    public void recover(Exception e, WorkflowStep workflowStep) {
        log.error("Unable to execute failure " + workflowStep.getUrl() + " action: {}", e.getMessage());
        // TODO: 2/16/2024 Save workflowStep to database and send notification and also launch a new thread to execute the action with cron job
    }
}
