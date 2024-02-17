package sn.esmt.gesb.services.Impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.esmt.gesb.dto.TPODataDto;
import sn.esmt.gesb.dto.TPOWorkOrderDto;
import sn.esmt.gesb.dto.Workflow;
import sn.esmt.gesb.services.SagaOrchestratorService;
import sn.esmt.gesb.soam.EsbRootActionRequest;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestProcessor {

    private final RestTemplate restTemplate;
    private final SagaOrchestratorService sagaOrchestratorService;

    @Value("${esb.url}")
    private String ESB_BASE_URL;

    @Async
    public void processRequest(EsbRootActionRequest esbRootActionRequest) {
        log.info("Processing request: {}", esbRootActionRequest);
        try {
            TPODataDto tpoDataDto = restTemplate.postForObject(ESB_BASE_URL + "tpo-manager", esbRootActionRequest, TPODataDto.class);
            assert tpoDataDto != null;
            log.info("TPODataDto: {}", tpoDataDto.getTpo());
            Workflow workflow = restTemplate.postForObject(ESB_BASE_URL + "tpo-manager/" + tpoDataDto.getId() + "/mapping", esbRootActionRequest, Workflow.class);

            if (workflow == null || workflow.getWorkflowSteps().isEmpty()) {
                log.error("No steps found for TPOData: {}", tpoDataDto.getTpo());
                return;
            }
            sagaOrchestratorService.executeSaga(workflow.getWorkflowSteps(), "CALL_BACK_URL");
        } catch (Exception e) {
            log.error("Error processing request: {}", e.getMessage());
        }

    }
}
