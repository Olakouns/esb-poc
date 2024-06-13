package sn.esmt.gesb.tpo_manager.services;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sn.esmt.gesb.dto.ApiResponse;
import sn.esmt.gesb.tpo_manager.exceptions.BadRequestException;
import sn.esmt.gesb.tpo_manager.exceptions.RequestNotAcceptableException;
import sn.esmt.gesb.tpo_manager.exceptions.ResourceNotFoundException;
import sn.esmt.gesb.tpo_manager.models.ConstantConfig;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWorkOrder;
import sn.esmt.gesb.tpo_manager.models.TpoFailureState;
import sn.esmt.gesb.tpo_manager.models.chain.ListNode;
import sn.esmt.gesb.tpo_manager.repositories.ConstantConfigRepository;
import sn.esmt.gesb.tpo_manager.repositories.TPODataRepository;
import sn.esmt.gesb.tpo_manager.repositories.TPOWordOrderRepository;
import sn.esmt.gesb.tpo_manager.repositories.TpoFailureStateRepository;
import sn.esmt.gesb.tpo_manager.services.chain.ListNodeService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class TpoAdminServiceImpl implements TpoAdminService {


    private final TPODataRepository tpoDataRepository;
    private final TPOWordOrderRepository tpoWordOrderRepository;
    private final ListNodeService listNodeService;
    private final EntityManager entityManager;
    private final ConstantConfigRepository constantConfigRepository;
    private final TpoFailureStateRepository tpoFailureStateRepository;


    private Specification<TPOData> getSpecification(String search) {
//        specification = specification.or((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), criteriaBuilder.lower(criteriaBuilder.literal("%" + search + "%"))));

        Specification<TPOData> specification = (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("tpo")), criteriaBuilder.lower(criteriaBuilder.literal("%" + search + "%")));
        specification = specification.or((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("verb")), criteriaBuilder.lower(criteriaBuilder.literal("%" + search + "%"))));
        specification = specification.or((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("tpoCondition")), criteriaBuilder.lower(criteriaBuilder.literal("%" + search + "%"))));
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
        tpoDataDB.setCritical(tpoData.isCritical());
        // todo : to be reviewed
//        tpoDataDB.setTpoDataOnFailure(tpoData.getTpoDataOnFailure());
        tpoDataDB.setDescription(tpoData.getDescription());
        return tpoDataRepository.save(tpoDataDB);
    }

    @Override
    public TPOData getTpoDataById(int id) {
        TPOData tpoData = tpoDataRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", id));
        tpoData.setPatterns(new LinkedList<>());
        tpoData.setPreviousStatesData(new LinkedList<>());
        return tpoData;
    }

    @Override
    public ApiResponse deleteTpoData(int tpoDataId) {
        TPOData tpoData = tpoDataRepository.findById(tpoDataId).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId));
        tpoDataRepository.delete(tpoData);
        return new ApiResponse(true, "TPOData deleted successfully");
    }

    @Override
    public List<TPOWorkOrder> getAllTpoWordOrder(int tpoDataId) {
        List<TPOWorkOrder> tpoWorkOrders =  tpoDataRepository.findById(tpoDataId)
                .orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId)).getLinkedList();
        tpoWorkOrders.forEach(tpoWorkOrder -> tpoFailureStateRepository
                .findByTpoIdAndWoId(tpoDataId, tpoWorkOrder.getId())
                .ifPresent(tpoWorkOrder::setTpoFailureState));
        return tpoWorkOrders;

    }

    @Override
    public TPOWorkOrder addTpoWordOrder(int tpoDataId, TPOWorkOrder tpoWordOrder) {
        if (tpoWordOrder.getId() == 0) {
            tpoWordOrder = tpoWordOrderRepository.save(tpoWordOrder);
        }
        TPOData tpoData = tpoDataRepository.findById(tpoDataId).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId));
        tpoData.getPatterns().add(tpoWordOrder);

        if (tpoData.getListNode() != null) {
            listNodeService.addEnd(tpoData.getListNode(), tpoWordOrder);
        } else {
            ListNode listNode = listNodeService.createListNode(tpoWordOrder);
            tpoData.setListNode(listNode);
        }
        tpoDataRepository.save(tpoData);
        return tpoWordOrder;
    }

    @Override
    public ApiResponse addManyTpoWordOrder(int tpoDataId, List<TPOWorkOrder> tpoWordOrders) {
//        if (!tpoDataRepository.existsById(tpoDataId)) {
//            throw new ResourceNotFoundException("TPOData", "id", tpoDataId);
//        }

        TPOData tpoData = tpoDataRepository.findById(tpoDataId).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId));
        for (TPOWorkOrder tpoWordOrder : tpoWordOrders) {
            tpoData.getPatterns().add(tpoWordOrder);
        }

        tpoData.setListNode(null);
        tpoDataRepository.save(tpoData);
        tpoData.setListNode(listNodeService.createListNode(new LinkedList<>(tpoData.getPatterns())));
        tpoDataRepository.save(tpoData);
        return new ApiResponse(true, "TPOWordOrder added successfully");
    }

    @Override
    public ApiResponse addTpoWordOrderById(int tpoDataId, int tpoWordOrderId) {
        TPOData tpoData = tpoDataRepository.findById(tpoDataId).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId));
        TPOWorkOrder tpoWordOrder = tpoWordOrderRepository.findById(tpoWordOrderId).orElseThrow(() -> new ResourceNotFoundException("TPOWordOrder", "id", tpoWordOrderId));
        tpoData.getPatterns().add(tpoWordOrder);
        return new ApiResponse(true, "TPOWordOrder added successfully");
    }

    @Override
    public ApiResponse removeTpoWordOrder(int tpoDataId, int tpoWordOrderId) {
        TPOData tpoData = tpoDataRepository.findById(tpoDataId).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId));
        tpoData.getPatterns().removeIf(tpoWordOrder -> tpoWordOrder.getId() == tpoWordOrderId);
        tpoData.setListNode(null);
        tpoDataRepository.save(tpoData);
        entityManager.detach(tpoData);
        if (tpoData.getPatterns().isEmpty()) {
            return new ApiResponse(true, "TPOWordOrder removed successfully");
        }
        ListNode node = listNodeService.createListNode(new LinkedList<>(tpoData.getPatterns()));
        tpoData.setListNode(node);
        tpoDataRepository.save(tpoData);
        return new ApiResponse(true, "TPOWordOrder removed successfully");
    }

    @Override
    public TPOWorkOrder updateTpoWordOrder(int tpoWordOrderId, TPOWorkOrder tpoWordOrder) {
        TPOWorkOrder tpoWordOrderDB = tpoWordOrderRepository.findById(tpoWordOrderId).orElseThrow(() -> new ResourceNotFoundException("TPOWordOrder", "id", tpoWordOrderId));
        tpoWordOrderDB.setEquipment(tpoWordOrder.getEquipment());
        tpoWordOrderDB.setTemplate(tpoWordOrder.getTemplate());
        tpoWordOrderDB.setServiceTemplate(tpoWordOrder.isServiceTemplate());
        tpoWordOrderDB.setWebServiceName(tpoWordOrder.getWebServiceName());
        return tpoWordOrderRepository.save(tpoWordOrderDB);
    }

    @Override
    public ApiResponse deleteTpoWordOrder(int tpoWordOrderId) {
        TPOWorkOrder tpoWordOrder = tpoWordOrderRepository.findById(tpoWordOrderId).orElseThrow(() -> new ResourceNotFoundException("TPOWordOrder", "id", tpoWordOrderId));
        tpoWordOrderRepository.delete(tpoWordOrder);
        return new ApiResponse(true, "TPOWordOrder deleted successfully");
    }

//    @Override
//    public TPOWorkOrder addTpoWordOrderFailureToWK(int tpoWordOrderId, TPOWorkOrder tpoWordOrder) {
//        TPOWorkOrder tpoWordOrderDB = tpoWordOrderRepository.findById(tpoWordOrderId).orElseThrow(() -> new ResourceNotFoundException("TPOWordOrder", "id", tpoWordOrderId));
//        tpoWordOrder = tpoWordOrderRepository.save(tpoWordOrder);
//        tpoWordOrderDB.getTpoWorkOrderFailure().add(tpoWordOrder);
//        return tpoWordOrder;
//    }

    @Override
    public ApiResponse updateTpoDataPatterns(int id, LinkedList<TPOWorkOrder> tpoWorkOrders) {
        TPOData tpoData = tpoDataRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", id));
        entityManager.detach(tpoData);
        tpoData.setPatterns(tpoWorkOrders);
        if (tpoData.getListNode() != null) {
            ListNode node = tpoData.getListNode();
            tpoData.setListNode(null);
            tpoData = tpoDataRepository.save(tpoData);
            entityManager.detach(tpoData);
            listNodeService.deleteListNode(node.getId());
        }
        tpoData.setListNode(listNodeService.createListNode(tpoWorkOrders));
        tpoDataRepository.save(tpoData);
        return new ApiResponse(true, "TPOData updated successfully");
    }

    @Override
    public List<TPOWorkOrder> getAllTpoWordOrders() {
        return tpoWordOrderRepository.findAll();
    }

    @Override
    public List<ConstantConfig> getAllConstantConfig() {
        return constantConfigRepository.findAll();
    }

    @Override
    public ConstantConfig createConstantConfig(ConstantConfig constantConfig) {
        if (constantConfigRepository.existsByKeyName(constantConfig.getKeyName())) {
            throw new RequestNotAcceptableException(constantConfig.getKeyName() + " already exist");
        }
        return constantConfigRepository.save(constantConfig);
    }

    @Override
    public ConstantConfig updateConstantConfig(int id, ConstantConfig constantConfig) {
        ConstantConfig constantConfigDb = constantConfigRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ConstantConfig", "id", id));
        constantConfigDb.setValueContent(constantConfig.getValueContent());
        constantConfigDb.setDescription(constantConfig.getDescription());

        if ((!constantConfigDb.getKeyName().equals(constantConfig.getKeyName())) && constantConfigRepository.existsByKeyName(constantConfig.getKeyName())) {
            throw new RequestNotAcceptableException(constantConfig.getKeyName() + " already exist");
        }
        constantConfigDb.setKeyName(constantConfig.getKeyName());
        return constantConfigRepository.save(constantConfigDb);
    }

    @Override
    public ApiResponse deleteConstantConfig(int id) {
        if (!constantConfigRepository.existsById(id)) {
            throw new ResourceNotFoundException("ConstantConfig", "id", id);
        }
        constantConfigRepository.deleteById(id);
        return new ApiResponse(true, "ConstantConfig deleted successfully");
    }

    @Override
    public TPOWorkOrder addWordOrder(TPOWorkOrder tpoWordOrder) {
        return tpoWordOrderRepository.save(tpoWordOrder);
    }

    @Override
    public TpoFailureState addTpoForWOFailureTo(int tpoWordOrderId, int tpoDataId, int tpoFailureId) {
        TPOWorkOrder tpoWordOrder = tpoWordOrderRepository.findById(tpoWordOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("TPOWordOrder", "id", tpoWordOrderId));
        TPOData tpoData = tpoDataRepository.findById(tpoDataId)
                .orElseThrow(() -> new ResourceNotFoundException("TPOData", "id", tpoDataId));

        if (tpoFailureStateRepository.existsByTpoIdAndWoId(tpoDataId, tpoWordOrderId)) {
            throw new BadRequestException("Tpo already associate to word order");
        }

        if (!tpoDataRepository.existsById(tpoFailureId)){
            throw  new ResourceNotFoundException("TPOData", "id", tpoFailureId);
        }

        TpoFailureState tpoFailureState = new TpoFailureState();
        tpoFailureState.setTpoId(tpoDataId);
        tpoFailureState.setWoId(tpoWordOrderId);
        tpoFailureState.setTpoFailureId(tpoFailureId);
        tpoFailureStateRepository.save(tpoFailureState);
        return tpoFailureState;
    }

    @Override
    public TpoFailureState updateFailureTpo(int tpoFailureStateId, int woId, int tpoDataId, int tpoFailureId) {
        TpoFailureState tpoFailureState = tpoFailureStateRepository.findById(tpoFailureStateId)
                .orElseThrow(() -> new ResourceNotFoundException("TpoFailureState", "id", tpoFailureStateId));
        tpoFailureState.setTpoFailureId(tpoFailureId);
        return tpoFailureStateRepository.save(tpoFailureState);
    }

    @Override
    public ApiResponse deleteFailureTpo(int tpoFailureStateId, int tpoDataId) {
        TpoFailureState tpoFailureState = tpoFailureStateRepository.findById(tpoFailureStateId)
                .orElseThrow(() -> new ResourceNotFoundException("TpoFailureState", "id", tpoFailureStateId));
        tpoFailureStateRepository.deleteById(tpoFailureStateId);
        return new ApiResponse(true, "Failure state deleted successfully");
    }

    //    @Override
//    public ApiResponse addTpoWordOrdersFailureToWK(int tpoWordOrderId, List<TPOWorkOrder> tpoWordOrders) {
//        TPOWorkOrder tpoWorkOrder = tpoWordOrderRepository.findById(tpoWordOrderId).orElseThrow(() -> new ResourceNotFoundException("TPOWorkOrder", "id", tpoWordOrderId));
//        tpoWorkOrder.setTpoWorkOrderFailure(tpoWordOrders);
//        tpoWordOrderRepository.save(tpoWorkOrder);
//        if (!tpoWordOrders.isEmpty()) {
//            tpoWorkOrder.setListNode(listNodeService.createListNode(new LinkedList<>(tpoWorkOrder.getTpoWorkOrderFailure())));
//        } else {
//            tpoWorkOrder.setListNode(null);
//        }
//        tpoWordOrderRepository.save(tpoWorkOrder);
//        return new ApiResponse(true, "TPOWordOrder added successfully");
//    }

    @Override
    public Page<TPOWorkOrder> getWordOrders(String search, int page, int size) {
        Specification<TPOWorkOrder> specification = (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("webServiceName")), criteriaBuilder.lower(criteriaBuilder.literal("%" + search + "%")));
        specification = specification.or((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("equipment")), criteriaBuilder.lower(criteriaBuilder.literal("%" + search + "%"))));
        Page<TPOWorkOrder> tpoWorkOrders = tpoWordOrderRepository.findAll(specification, PageRequest.of(page, size));
        tpoWorkOrders.forEach(tpoWorkOrder -> {
            // todo: check if work order is used by some tpo.
        });
        return tpoWorkOrders;
    }
}
