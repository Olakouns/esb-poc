package sn.esmt.gesb.tpo_manager.services;

import org.springframework.data.domain.Page;
import sn.esmt.gesb.dto.ApiResponse;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWorkOrder;

import java.util.List;

public interface TpoAdminService {
    List<TPOData> getAllTpoData(String search);

    Page<TPOData> getTpoData(String search, int page, int size);

    TPOData createTpoData(TPOData tpoData);

    TPOData updateTpoData(int id, TPOData tpoData);

    ApiResponse deleteTpoData(int id);


    List<TPOWorkOrder> getAllTpoWordOrder(int tpoDataId);

    ApiResponse addTpoWordOrder(int tpoDataId, TPOWorkOrder tpoWordOrder);
    ApiResponse addTpoWordOrderById(int tpoDataId, int tpoWordOrderId);
    ApiResponse removeTpoWordOrder(int tpoDataId, int tpoWordOrderId);

    TPOWorkOrder updateTpoWordOrder(int tpoWordOrderId, TPOWorkOrder tpoWordOrder);

    ApiResponse deleteTpoWordOrder(int tpoWordOrderId);
}
