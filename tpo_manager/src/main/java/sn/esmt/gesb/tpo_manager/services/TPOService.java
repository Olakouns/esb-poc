package sn.esmt.gesb.tpo_manager.services;

import sn.esmt.gesb.dto.TPODataDto;
import sn.esmt.gesb.dto.Workflow;
import sn.esmt.gesb.dto.WorkflowStep;
import sn.esmt.gesb.soam.EsbRootActionRequest;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWorkOrder;

import java.util.List;

public interface TPOService {
    TPODataDto getTPODataOfRequest(EsbRootActionRequest esbRootActionRequest);

    Workflow getMappingData(int tpoId, EsbRootActionRequest esbRootActionRequest);

    WorkflowStep getMappingDataCritical(int tpoId, EsbRootActionRequest esbRootActionRequest);
}