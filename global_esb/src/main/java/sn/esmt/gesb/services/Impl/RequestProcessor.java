package sn.esmt.gesb.services.Impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sn.esmt.gesb.components.QueueManagerComponent;
import sn.esmt.gesb.soam.EsbRootActionRequest;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestProcessor {
    public void processRequest(EsbRootActionRequest esbRootActionRequest) {
        log.info("Processing request: {}", esbRootActionRequest);
    }
}
