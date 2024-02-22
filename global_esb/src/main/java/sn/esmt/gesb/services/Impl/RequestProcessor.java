package sn.esmt.gesb.services.Impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.esmt.gesb.components.CurrentStateComponent;
import sn.esmt.gesb.critical.DisplaySubscriberResponse;
import sn.esmt.gesb.critical.SubscriberData;
import sn.esmt.gesb.dto.TPODataDto;
import sn.esmt.gesb.dto.Workflow;
import sn.esmt.gesb.dto.WorkflowStep;
import sn.esmt.gesb.services.SagaOrchestratorService;
import sn.esmt.gesb.soam.*;
import sn.esmt.gesb.utils.SoapResponseParser;

import java.util.Optional;
import java.util.OptionalLong;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestProcessor {

    private final RestTemplate restTemplate;
    private final SagaOrchestratorService sagaOrchestratorService;
    private final CurrentStateComponent currentStateComponent;
    private final SoapClientService soapClientService;


    @Value("${esb.url}")
    private String ESB_BASE_URL;

    @Async
    public void processRequest(EsbRootActionRequest esbRootActionRequest) {
        log.info("Processing request: {}", esbRootActionRequest);
        try {
            TPODataDto tpoDataDto = restTemplate.postForObject(ESB_BASE_URL + "tpo-manager", esbRootActionRequest, TPODataDto.class);
            assert tpoDataDto != null;
            log.info("TPODataDto: {}", tpoDataDto.getTpo());

            if (tpoDataDto.isCritical()) {
                Workflow critical = restTemplate.postForObject(ESB_BASE_URL + "tpo-manager/" + tpoDataDto.getId() + "/mapping/critical", esbRootActionRequest, Workflow.class);
                assert critical != null;
                for (WorkflowStep workflowStep : critical.getWorkflowSteps()) {
                    esbRootActionRequest = getCurrentState(workflowStep, esbRootActionRequest);
                }
            }

            Workflow workflow = restTemplate.postForObject(ESB_BASE_URL + "tpo-manager/" + tpoDataDto.getId() + "/mapping", esbRootActionRequest, Workflow.class);

            System.out.println(workflow);
            if (workflow == null || workflow.getWorkflowSteps().isEmpty()) {
                log.error("No steps found for TPOData: {}", tpoDataDto.getTpo());
                return;
            }

            sagaOrchestratorService.executeSaga(workflow.getWorkflowSteps(), "CALL_BACK_URL");
        } catch (Exception e) {
            log.error("Error processing request: {}", e.getMessage());
        }
    }

    public EsbRootActionRequest getCurrentState(WorkflowStep workflowStep, EsbRootActionRequest esbRootActionRequestFromUser) throws Exception {
        String domResult = soapClientService.sendSoapRequestGettingString(workflowStep.getUrl(), workflowStep.getBodyContent());
        Object result = SoapResponseParser.parse(workflowStep.getWebServiceClassName(),domResult);

        if (result instanceof SubscriberData){
            formatUserSate1((SubscriberData) result, esbRootActionRequestFromUser);
        } else if (result instanceof DisplaySubscriberResponse) {
            EsbContent esbContent = currentStateComponent.getEsbParameters((DisplaySubscriberResponse) result);
            formatUserSate2(esbContent, esbRootActionRequestFromUser);
        } else {
            // todo : more action here
        }
        return esbRootActionRequestFromUser;
    }

    private void formatUserSate1(SubscriberData subscriberData, EsbRootActionRequest esbRootActionRequestFromUser){
        EsbContent esbContent = currentStateComponent.getEsbParameters(subscriberData);
        VerbType verbType = esbRootActionRequestFromUser.getEsbContent().getVerb();
        for (EsbParameter esbParameter : esbContent.getEsbParameter()) {
            Optional<EsbParameter> parameter = esbRootActionRequestFromUser
                    .getEsbContent()
                    .getEsbParameter()
                    .stream()
                    .filter(esbParameter1 -> esbParameter1.getName().equals(esbParameter.getName()))
                    .findFirst();
            if (parameter.isEmpty()) {
                esbRootActionRequestFromUser.getEsbContent().getEsbParameter().add(esbParameter);
            } else {
                parameter.get().setOldValue(esbParameter.getNewValue());
            }
        }

        if (esbContent.getEsbServices() == null || esbContent.getEsbServices().getEsbService().isEmpty()) {
            return ;
        }

        if (esbRootActionRequestFromUser.getEsbContent().getEsbServices() == null) {
            esbRootActionRequestFromUser.getEsbContent().setEsbServices(new EsbServices());
        }

        for (EsbService esbService : esbContent.getEsbServices().getEsbService()) {
            Optional<EsbParameter> parameter = esbService.getEsbParameter().stream().filter(esbParameter -> esbParameter.getName().equals("serviceType")).findFirst();
            if (parameter.isEmpty()) continue;

            Optional<EsbService> esbServiceB = esbRootActionRequestFromUser
                    .getEsbContent()
                    .getEsbServices()
                    .getEsbService()
                    .stream().filter(esbService1 -> {
                        Optional<EsbParameter> parameter2 = esbService.getEsbParameter().stream().filter(esbParameter -> esbParameter.getNewValue().equals("serviceType")).findFirst();
                        return parameter2.filter(esbParameter -> parameter.get().getNewValue().equals(esbParameter.getNewValue())).isPresent();
                    }).findFirst();

            if (esbServiceB.isEmpty()) {
                esbRootActionRequestFromUser
                        .getEsbContent()
                        .getEsbServices()
                        .getEsbService()
                        .add(esbService);
            } else {
//                todo : Implement to toggle value
            }

        }
    }

    private void formatUserSate2(EsbContent esbContent, EsbRootActionRequest esbRootActionRequestFromUser){
        for (EsbParameter esbParameter : esbContent.getEsbParameter()) {
            Optional<EsbParameter> parameter = esbRootActionRequestFromUser
                    .getEsbContent()
                    .getEsbParameter()
                    .stream()
                    .filter(esbParameter1 -> esbParameter1.getName().equals(esbParameter.getName()))
                    .findFirst();
            if (parameter.isEmpty()) {
                esbRootActionRequestFromUser.getEsbContent().getEsbParameter().add(esbParameter);
            } else {
                parameter.get().setOldValue(esbParameter.getNewValue());
            }
        }
    }
}
