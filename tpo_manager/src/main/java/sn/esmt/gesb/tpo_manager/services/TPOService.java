package sn.esmt.gesb.tpo_manager.services;

import sn.esmt.gesb.dto.TPODataDto;
import sn.esmt.gesb.soam.EsbRootActionRequest;
import sn.esmt.gesb.tpo_manager.models.TPOData;

public interface TPOService {
    TPODataDto getTPODataOfRequest(EsbRootActionRequest esbRootActionRequest);
}