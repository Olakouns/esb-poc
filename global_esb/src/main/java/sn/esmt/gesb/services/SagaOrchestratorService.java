package sn.esmt.gesb.services;

import sn.esmt.gesb.dto.TPOWorkOrderDto;

import java.util.List;

public interface SagaOrchestratorService {
    void executeSaga(List<TPOWorkOrderDto> workOrders, String callbackURL);
}
