package sn.esmt.gesb.services.Impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import sn.esmt.gesb.dto.WorkflowStep;

@Service
@AllArgsConstructor
@Slf4j
public class RequestRetryForFailure {

    private final SoapClientService soapClientService;

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
