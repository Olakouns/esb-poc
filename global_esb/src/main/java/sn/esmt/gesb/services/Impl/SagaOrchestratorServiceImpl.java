package sn.esmt.gesb.services.Impl;

import org.springframework.stereotype.Service;
import sn.esmt.gesb.dto.TPOWorkOrderDto;
import sn.esmt.gesb.services.SagaOrchestratorService;

import java.util.LinkedList;
import java.util.List;
@Service
public class SagaOrchestratorServiceImpl implements SagaOrchestratorService {

    @Override
    public void executeSaga(List<TPOWorkOrderDto> workOrders, String callbackURL) {
        for (TPOWorkOrderDto workOrder : workOrders) {
            try {
                // todo : send request here
                throw new Exception("Error");
            } catch (Exception exception) {
                this.rollback(workOrders, workOrder, callbackURL);
            }
        }
    }

    private void rollback(List<TPOWorkOrderDto> actions, TPOWorkOrderDto tpoWorkOrderFailure, String callbackURL) {
        int index = actions.indexOf(tpoWorkOrderFailure);

//        if (index == -1) throw new Exception("Some ");
        if (index == -1 || index == 0) return;
        List<TPOWorkOrderDto> tpoWorkOrders = new LinkedList<>();
        for (int i = 0; i < index - 1; i++) {
            for (int j = actions.get(i).getTpoWorkOrderFailure().size() - 1; j >= 0; j--) {
                tpoWorkOrders.add(actions.get(i).getTpoWorkOrderFailure().get(j));
            }
        }

        if (!tpoWorkOrders.isEmpty()) {
            this.executeSaga(tpoWorkOrders, callbackURL);
        }
    }
}
