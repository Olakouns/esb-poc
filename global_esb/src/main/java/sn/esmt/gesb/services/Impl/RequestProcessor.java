package sn.esmt.gesb.services.Impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.esmt.gesb.dto.TPODataDto;
import sn.esmt.gesb.services.SagaOrchestratorService;
import sn.esmt.gesb.soam.EsbRootActionRequest;


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
        try{
            TPODataDto tpoDataDto = restTemplate.postForObject(ESB_BASE_URL + "tpo-manager", esbRootActionRequest, TPODataDto.class);
            assert tpoDataDto != null;
            log.info("TPODataDto: {}", tpoDataDto.getTpo());
            // todo: make mapping before send data
            sagaOrchestratorService.executeSaga(tpoDataDto.getPatterns(), "CALL_BACK_URL");
        }catch (Exception e){
            log.error("Error processing request: {}", e.getMessage());
        }

    }
}
