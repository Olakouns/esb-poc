package sn.esmt.gesb.tpo_manager.services;

import org.springframework.data.domain.Page;
import sn.esmt.gesb.dto.ApiResponse;
import sn.esmt.gesb.tpo_manager.models.ConstantConfig;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWorkOrder;

import java.util.LinkedList;
import java.util.List;

public interface TpoAdminService {
    List<TPOData> getAllTpoData(String search);

    Page<TPOData> getTpoData(String search, int page, int size);

    TPOData createTpoData(TPOData tpoData);

    TPOData updateTpoData(int id, TPOData tpoData);

    TPOData getTpoDataById(int id);


    ApiResponse deleteTpoData(int id);


    List<TPOWorkOrder> getAllTpoWordOrder(int tpoDataId);

    TPOWorkOrder addTpoWordOrder(int tpoDataId, TPOWorkOrder tpoWordOrder);
    ApiResponse addManyTpoWordOrder(int tpoDataId, List<TPOWorkOrder> tpoWordOrders);
    ApiResponse addTpoWordOrderById(int tpoDataId, int tpoWordOrderId);
    ApiResponse removeTpoWordOrder(int tpoDataId, int tpoWordOrderId);

    TPOWorkOrder updateTpoWordOrder(int tpoWordOrderId, TPOWorkOrder tpoWordOrder);

    ApiResponse deleteTpoWordOrder(int tpoWordOrderId);

//    TPOWorkOrder addTpoWordOrderFailureToWK(int tpoWordOrderId, TPOWorkOrder tpoWordOrder);

    ApiResponse updateTpoDataPatterns(int id, LinkedList<TPOWorkOrder> tpoData);

    List<TPOWorkOrder> getAllTpoWordOrders();

    List<ConstantConfig> getAllConstantConfig();

    ConstantConfig createConstantConfig(ConstantConfig constantConfig);

    ConstantConfig updateConstantConfig(int id, ConstantConfig constantConfig);

    ApiResponse deleteConstantConfig(int id);

    TPOWorkOrder addWordOrder(TPOWorkOrder tpoWordOrder);

    ApiResponse addTpoForWOFailureTo(int tpoWordOrderId, int tpoDataId, int tpoFailureId);
//    ApiResponse addTpoWordOrdersFailureToWK(int tpoWordOrderId, List<TPOWorkOrder> tpoWordOrders);

    Page<TPOWorkOrder> getWordOrders(String search, int page, int size);
}
