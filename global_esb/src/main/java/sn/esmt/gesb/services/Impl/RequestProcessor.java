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
import sn.esmt.gesb.utils.XmlParser;

import java.util.Date;
import java.util.List;
import java.util.Optional;


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

//    @Async
    public EsbRootActionResponse processRequest(EsbRootActionRequest esbRootActionRequest) {
        EsbRootActionResponse esbRootActionResponse = new EsbRootActionResponse();
        log.info("Processing request: {} at {}", esbRootActionRequest.getRequestId(), new Date().getTime());
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

//            System.err.println(workflow);

            if (workflow == null || workflow.getWorkflowSteps().isEmpty()) {
                log.error("No steps found for TPOData: {}", tpoDataDto.getTpo());
                esbRootActionResponse.setSuccess(false);
                esbRootActionResponse.setMessage("No steps found for TPOData: " + tpoDataDto.getTpo());
                return esbRootActionResponse;
            }

            return sagaOrchestratorService.executeSaga(workflow.getWorkflowSteps(), "CALL_BACK_URL", esbRootActionRequest.getRequestId());
        } catch (Exception e) {
            log.error("Error processing request {} : {}", esbRootActionRequest.getRequestId(), e.getMessage());
            esbRootActionResponse.setSuccess(false);
            esbRootActionResponse.setMessage(String.format("Error processing request %s : %s " , esbRootActionRequest.getRequestId(), e.getMessage()));
            return esbRootActionResponse;
        }
    }

    public EsbRootActionRequest getCurrentState(WorkflowStep workflowStep, EsbRootActionRequest esbRootActionRequestFromUser) throws Exception {
        String domResult = soapClientService.sendSoapRequestGettingString(workflowStep.getUrl(), workflowStep.getBodyContent());

        XmlParser parser = new XmlParser();
        EsbRootActionRequest esbRootActionRequestData = parser.parseXml(domResult, workflowStep.getWebServiceClassName());
        for (EsbParameter esbParameter : esbRootActionRequestData.getEsbContent().getEsbParameter()) {
            esbRootActionRequestFromUser
                    .getEsbContent()
                    .getEsbParameter()
                    .stream()
                    .filter(esbParameter1 -> esbParameter1.getName().equals(esbParameter.getName()))
                    .findFirst()
                    .ifPresentOrElse(
                            p -> p.setOldValue(esbParameter.getOldValue()),
                            () -> esbRootActionRequestFromUser
                                    .getEsbContent()
                                    .getEsbParameter().add(esbParameter)
                    );
        }

        if (esbRootActionRequestFromUser.getEsbContent().getEsbServices() == null) {
            esbRootActionRequestFromUser.getEsbContent().setEsbServices(new EsbServices());
        }

        if (!esbRootActionRequestData.getEsbContent().getEsbServices().getEsbService().isEmpty()) {
            List<EsbService> esbServiceList = esbRootActionRequestData.getEsbContent().getEsbServices().getEsbService();
            for (EsbService esbService : esbServiceList) {
                Optional<EsbParameter> parameter = esbService
                        .getEsbParameter()
                        .stream()
                        .filter(esbParameter -> esbParameter.getName().equals("serviceType")).findFirst();
                if (parameter.isEmpty()) continue;

                Optional<EsbService> esbServiceB = esbRootActionRequestFromUser
                        .getEsbContent()
                        .getEsbServices()
                        .getEsbService()
                        .stream().filter(esbService1 -> {
                            Optional<EsbParameter> parameter2 = esbService1.getEsbParameter().stream().filter(esbParameter -> esbParameter.getName().equals("serviceType")).findFirst();
                            return parameter2.filter(esbParameter -> parameter.get().getOldValue().equals(esbParameter.getNewValue())).isPresent();
                        }).findFirst();

                if (esbServiceB.isEmpty()) {
                    esbRootActionRequestFromUser
                            .getEsbContent()
                            .getEsbServices()
                            .getEsbService()
                            .add(esbService);
                } else {
                    // todo : update esbservice
                    esbServiceB.get()
                            .getEsbParameter()
                            .stream()
                            .map(esbParameter -> {
                                esbService
                                        .getEsbParameter()
                                        .stream()
                                        .filter(esbParameter1 -> esbParameter1.getName().equals(esbParameter.getName())).findFirst()
                                .ifPresent(value -> esbParameter.setOldValue(value.getOldValue()));
                                return  esbParameter;
                            });

                }
            }
        }

//        Object result = SoapResponseParser.parse(workflowStep.getWebServiceClassName(), domResult);
//
//        if (result instanceof SubscriberData) {
//            formatUserSate1((SubscriberData) result, esbRootActionRequestFromUser);
//        } else if (result instanceof DisplaySubscriberResponse) {
//            EsbContent esbContent = currentStateComponent.getEsbParameters((DisplaySubscriberResponse) result);
//            formatUserSate2(esbContent, esbRootActionRequestFromUser);
//        } else {
//            // todo : more action here
//        }
        return esbRootActionRequestFromUser;
    }

    // TODO:  Les deux fonctions suivantes sont a revoir !!
    private void formatUserSate1(SubscriberData subscriberData, EsbRootActionRequest esbRootActionRequestFromUser) {
        EsbContent esbContent = currentStateComponent.getEsbParameters(subscriberData);
//        VerbType verbType = esbRootActionRequestFromUser.getEsbContent().getVerb();
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
            return;
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

    private void formatUserSate2(EsbContent esbContent, EsbRootActionRequest esbRootActionRequestFromUser) {
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
