package sn.esmt.gesb.services.Impl;

import jakarta.xml.soap.Detail;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.ws.soap.SOAPFaultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultException;
import org.springframework.ws.soap.client.SoapFaultClientException;
import sn.esmt.gesb.dto.WorkflowStep;
import sn.esmt.gesb.services.SagaOrchestratorService;
import sn.esmt.gesb.soam.EsbRootActionResponse;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SagaOrchestratorServiceImpl implements SagaOrchestratorService {

    private final SoapClientService soapClientService;
    private final RequestRetryForFailure requestRetry;

    @Override
    public EsbRootActionResponse executeSaga(List<WorkflowStep> workflowSteps, String callbackURL, String requestId) {
        EsbRootActionResponse esbRootActionResponse = new EsbRootActionResponse();
        for (WorkflowStep workflowStep : workflowSteps) {
            try {
                // todo : send request here
                soapClientService.sendSoapRequest(workflowStep.getUrl(), workflowStep.getBodyContent());
                esbRootActionResponse.setSuccess(true);
                esbRootActionResponse.setMessage("operation successfully completed");
            } catch (Exception exception) {
                esbRootActionResponse.setSuccess(false);
                esbRootActionResponse.setMessage(exception.getMessage());
                if (exception instanceof SoapFaultClientException soapFaultException) {
                    // todo : show message correctly here
                    /*
                    SoapFault soapFault = soapFaultException.getSoapFault();
                    log.error("getFaultCode " + soapFault.getFaultCode());
                    log.error("getFaultDetail " + soapFault.getFaultDetail());
                    log.error("getFaultStringOrReason " + soapFault.getFaultStringOrReason());
                    log.error("getFaultStringOrReason " + soapFaultException);
                    */
                    throw soapFaultException;
                } else {
                    log.error(workflowStep.getUrl() + " " + exception.getMessage());
                }
                this.rollback(workflowStep.getFailureSteps(), callbackURL);
                break;
            }
        }
        log.info("End request {} treatment  at {}", requestId, new Date().getTime());
        return esbRootActionResponse;
    }

    private void rollback(List<WorkflowStep> workflowSteps, String callbackURL) {
        for (WorkflowStep action : workflowSteps) {
            requestRetry.executeFailure(action);
        }
    }

//    private void rollback(List<WorkflowStep> workflowSteps, int index, String callbackURL) {
//        if (index == -1) throw new RuntimeException("No rollback action found");
//        if (index == 0) {
//            log.error("Nothing to rollback");
//            return;
//        }
//
//        if (workflowSteps.get(index -1).getFailureSteps().isEmpty()) return;
//        for (WorkflowStep action : workflowSteps.get(index -1).getFailureSteps()) {
//            requestRetry.executeFailure(action);
//        }
//    }

//    private void rollback(List<WorkflowStep> workflowSteps, int index, String callbackURL) {
//        if (index == -1) throw new RuntimeException("No rollback action found");
//        if (index == 0) {
//            log.error("Nothing to rollback");
//        }
//        List<WorkflowStep> actions = new LinkedList<>();
//        for (int i = index - 1; i >= 0; i--) {
//            if (workflowSteps.get(i).getFailureSteps() == null) continue;
//            // Collections.reverse(workflowSteps.get(i).getFailureSteps());
//            actions.addAll(workflowSteps.get(i).getFailureSteps());
//        }
//
//        for (WorkflowStep action : actions) {
//            requestRetry.executeFailure(action);
//        }
//        // TODO: 2/16/2024 send callback
//    }
}
