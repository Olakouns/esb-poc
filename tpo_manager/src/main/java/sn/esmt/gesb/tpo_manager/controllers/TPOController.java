package sn.esmt.gesb.tpo_manager.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.esmt.gesb.dto.TPODataDto;
import sn.esmt.gesb.soam.EsbRootActionRequest;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.services.TPOService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/tpo-manager")
public class TPOController {

    private final TPOService tpoService;

    @PostMapping
    public TPODataDto getTpoData(@RequestBody EsbRootActionRequest esbRootActionRequest) {
        log.info("Receive client request "+esbRootActionRequest.getRequestId());
        return tpoService.getTPODataOfRequest(esbRootActionRequest);
    }
}
