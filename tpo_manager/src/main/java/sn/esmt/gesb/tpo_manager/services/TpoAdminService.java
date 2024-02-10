package sn.esmt.gesb.tpo_manager.services;

import org.springframework.data.domain.Page;
import sn.esmt.gesb.dto.ApiResponse;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWordOrder;

import java.util.List;

public interface TpoAdminService {
    List<TPOData> getAllTpoData(String search);

    Page<TPOData> getTpoData(String search, int page, int size);

    TPOData createTpoData(TPOData tpoData);

    TPOData updateTpoData(int id, TPOData tpoData);

    ApiResponse deleteTpoData(int id);


    List<TPOWordOrder> getAllTpoWordOrder(int tpoDataId);

    ApiResponse addTpoWordOrder(int tpoDataId, TPOWordOrder tpoWordOrder);
    ApiResponse addTpoWordOrderById(int tpoDataId, int tpoWordOrderId);
    ApiResponse removeTpoWordOrder(int tpoDataId, int tpoWordOrderId);

    TPOWordOrder updateTpoWordOrder(int tpoWordOrderId, TPOWordOrder tpoWordOrder);

    ApiResponse deleteTpoWordOrder(int tpoWordOrderId);
}
