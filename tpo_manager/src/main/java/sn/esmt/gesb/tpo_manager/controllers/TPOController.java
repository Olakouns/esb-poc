package sn.esmt.gesb.tpo_manager.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sn.esmt.gesb.dto.TPODataDto;
import sn.esmt.gesb.soam.EsbRootActionRequest;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWorkOrder;
import sn.esmt.gesb.tpo_manager.services.TPOService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/tpo-manager")
public class TPOController {

    private final TPOService tpoService;

    @PostMapping
    public TPODataDto getTpoData(@RequestBody EsbRootActionRequest esbRootActionRequest) {
        log.info("Start searching TPO" + esbRootActionRequest.getRequestId());
        return tpoService.getTPODataOfRequest(esbRootActionRequest);
    }

    @PutMapping("{tpoId}")
    public List<TPOWorkOrder> getMappingData(@PathVariable int tpoId, @RequestBody EsbRootActionRequest esbRootActionRequest) {
        log.info("Start tpo template mapping" + esbRootActionRequest.getRequestId());
        return tpoService.getMappingData(tpoId, esbRootActionRequest);
    }
}
