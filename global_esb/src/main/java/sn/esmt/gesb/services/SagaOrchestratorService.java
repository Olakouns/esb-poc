package sn.esmt.gesb.services;

import sn.esmt.gesb.dto.TPOWorkOrderDto;
import sn.esmt.gesb.dto.Workflow;
import sn.esmt.gesb.dto.WorkflowStep;
import sn.esmt.gesb.soam.EsbRootActionResponse;

import java.util.List;

public interface SagaOrchestratorService {
    EsbRootActionResponse executeSaga(List<WorkflowStep> workflowSteps, String callbackURL, String requestId);
}
