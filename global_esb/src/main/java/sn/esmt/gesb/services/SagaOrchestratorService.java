package sn.esmt.gesb.services;

import sn.esmt.gesb.dto.TPOWorkOrderDto;
import sn.esmt.gesb.dto.Workflow;
import sn.esmt.gesb.dto.WorkflowStep;

import java.util.List;

public interface SagaOrchestratorService {
    void executeSaga(List<WorkflowStep> workflowSteps, String callbackURL, String requestId);
}
