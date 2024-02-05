package sn.esmt.gesb.tpo_manager.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.esmt.gesb.dto.TPODataDto;
import sn.esmt.gesb.soam.EsbRootActionRequest;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.repositories.TPODataRepository;

@Service
@RequiredArgsConstructor
public class TPOServiceImpl implements TPOService {

    private final TPODataRepository tpoDataRepository;
    @Override
    public TPODataDto getTPODataOfRequest(EsbRootActionRequest esbRootActionRequest) {
        // todo : MAke request here
        return null;
    }
}
