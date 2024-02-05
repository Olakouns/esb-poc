package sn.esmt.gesb.services.Impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sn.esmt.gesb.dto.TPODataDto;
import sn.esmt.gesb.soam.EsbRootActionRequest;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestProcessor {

    private final RestTemplate restTemplate;

    @Value("${esb.url}")
    private String ESB_BASE_URL;

    public void processRequest(EsbRootActionRequest esbRootActionRequest) {
        log.info("Processing request: {}", esbRootActionRequest);
        TPODataDto tpoDataDto = restTemplate.postForObject(ESB_BASE_URL + "tpo-manager", esbRootActionRequest, TPODataDto.class);

    }
}
