package sn.esmt.gesb.tpo_manager.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sn.esmt.gesb.dto.ApiResponse;
import sn.esmt.gesb.tpo_manager.exceptions.ResourceNotFoundException;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWordOrder;
import sn.esmt.gesb.tpo_manager.repositories.TPODataRepository;
import sn.esmt.gesb.tpo_manager.repositories.TPOWordOrderRepository;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class TpoAdminServiceImpl implements TpoAdminService {


    private final TPODataRepository tpoDataRepository;
    private final TPOWordOrderRepository tpoWordOrderRepository;


    private Specification<TPOData> getSpecification(String search) {
        Specification<TPOData> specification = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("tpo"), "%" + search + "%");
        specification.or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("verb"), "%" + search + "%"));
        specification.or((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("condition"), "%" + search + "%"));
        return specification;
    }

    @Override
    public List<TPOData> getAllTpoData(String search) {
        return tpoDataRepository.findAll(getSpecification(search));
    }

    @Override
    public Page<TPOData> getTpoData(String search, int page, int size) {
        return tpoDataRepository.findAll(getSpecification(search), PageRequest.of(page, size));
    }

    @Override
    public TPOData createTpoData(TPOData tpoData) {
        return tpoDataRepository.save(tpoData);
    }

    @Override
    public TPOData updateTpoData(int tpoDataId, TPOData tpoData) {
        TPOData tpoDataDB = tpoDataRepository.findById(tpoDataId).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId));
        tpoDataDB.setTpo(tpoData.getTpo());
        tpoDataDB.setTpoCondition(tpoData.getTpoCondition());
        tpoDataDB.setTpoDataOnFailure(tpoData.getTpoDataOnFailure());
        tpoDataDB.setDescription(tpoData.getDescription());
        return tpoDataRepository.save(tpoDataDB);
    }

    @Override
    public ApiResponse deleteTpoData(int tpoDataId) {
        TPOData tpoData = tpoDataRepository.findById(tpoDataId).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId));
        tpoDataRepository.delete(tpoData);
        return new ApiResponse(true, "TPOData deleted successfully");
    }

    @Override
    public List<TPOWordOrder> getAllTpoWordOrder(int tpoDataId) {
        return tpoDataRepository.findById(tpoDataId).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId)).getPatterns();
    }

    @Override
    public ApiResponse addTpoWordOrder(int tpoDataId, TPOWordOrder tpoWordOrder) {
        if (tpoWordOrder.getId() == 0) {
            tpoWordOrder = tpoWordOrderRepository.save(tpoWordOrder);
        }
        return this.addTpoWordOrderById(tpoDataId, tpoWordOrder.getId());
    }

    @Override
    public ApiResponse addTpoWordOrderById(int tpoDataId, int tpoWordOrderId) {
        TPOData tpoData = tpoDataRepository.findById(tpoDataId).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId));
        TPOWordOrder tpoWordOrder = tpoWordOrderRepository.findById(tpoWordOrderId).orElseThrow(() -> new ResourceNotFoundException("TPOWordOrder", "id", tpoWordOrderId));
        tpoData.getPatterns().add(tpoWordOrder);
        return new ApiResponse(true, "TPOWordOrder added successfully");
    }

    @Override
    public ApiResponse removeTpoWordOrder(int tpoDataId, int tpoWordOrderId) {
        TPOData tpoData = tpoDataRepository.findById(tpoDataId).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId));
        tpoData.getPatterns().removeIf(tpoWordOrder -> tpoWordOrder.getId() == tpoWordOrderId);
        return new ApiResponse(true, "TPOWordOrder removed successfully");
    }

    @Override
    public TPOWordOrder updateTpoWordOrder(int tpoWordOrderId, TPOWordOrder tpoWordOrder) {
        TPOWordOrder tpoWordOrderDB = tpoWordOrderRepository.findById(tpoWordOrderId).orElseThrow(() -> new ResourceNotFoundException("TPOWordOrder", "id", tpoWordOrderId));
        tpoWordOrderDB.setEquipment(tpoWordOrder.getEquipment());
        tpoWordOrderDB.setTemplate(tpoWordOrder.getTemplate());
        tpoWordOrderDB.setWebServiceName(tpoWordOrder.getWebServiceName());
        return tpoWordOrderRepository.save(tpoWordOrderDB);
    }

    @Override
    public ApiResponse deleteTpoWordOrder(int tpoWordOrderId) {
        TPOWordOrder tpoWordOrder = tpoWordOrderRepository.findById(tpoWordOrderId).orElseThrow(() -> new ResourceNotFoundException("TPOWordOrder", "id", tpoWordOrderId));
        tpoWordOrderRepository.delete(tpoWordOrder);
        return new ApiResponse(true, "TPOWordOrder deleted successfully");
    }
}
