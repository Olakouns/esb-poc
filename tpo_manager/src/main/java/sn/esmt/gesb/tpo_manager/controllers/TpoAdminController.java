package sn.esmt.gesb.tpo_manager.controllers;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import sn.esmt.gesb.dto.ApiResponse;
import sn.esmt.gesb.tpo_manager.models.ConstantConfig;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWorkOrder;
import sn.esmt.gesb.tpo_manager.models.TpoFailureState;
import sn.esmt.gesb.tpo_manager.services.TpoAdminService;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/admin/tpo-manager")
public class TpoAdminController {
    private final TpoAdminService tpoAdminService;

    @GetMapping("/tpo-data")
    public List<TPOData> getAllTpoData(@RequestParam(required = false, defaultValue = "") String search) {
        return this.tpoAdminService.getAllTpoData(search);
    }

    @GetMapping("/tpo-data/page")
    public Page<TPOData> getTpoData(@RequestParam(required = false, defaultValue = "") String search,
                                    @RequestParam(required = false, defaultValue = "0") int page,
                                    @RequestParam(required = false, defaultValue = "50") int size) {
        return this.tpoAdminService.getTpoData(search, page, size);
    }

    @PostMapping("/tpo-data")
    public TPOData createTpoData(@RequestBody TPOData tpoData) {
        return tpoAdminService.createTpoData(tpoData);
    }

    @PutMapping("/tpo-data/{id}/edit-flow")
    public TPOData updateTpoData(@PathVariable int id, @RequestBody TPOData tpoData) {
        return tpoAdminService.updateTpoData(id, tpoData);
    }

    @GetMapping("/tpo-data/{id}")
    public TPOData getTpoDataById(@PathVariable int id) {
        return tpoAdminService.getTpoDataById(id);
    }

    @PutMapping("/tpo-data/{id}")
    public ApiResponse updateTpoDataPatterns(@PathVariable int id, @RequestBody LinkedList<TPOWorkOrder> tpoData) {
        return tpoAdminService.updateTpoDataPatterns(id, tpoData);
    }

    @DeleteMapping("/tpo-data/{id}")
    public ApiResponse deleteTpoData(@PathVariable int id) {
        return tpoAdminService.deleteTpoData(id);
    }


    @GetMapping("/tpo-data/{tpoDataId}/tpo-word-order")
    public List<TPOWorkOrder> getAllTpoWordOrder(@PathVariable int tpoDataId) {
        return tpoAdminService.getAllTpoWordOrder(tpoDataId);
    }

    @PutMapping("/tpo-data/{tpoDataId}/tpo-word-order")
    public TPOWorkOrder addTpoWordOrder(@PathVariable int tpoDataId, @RequestBody TPOWorkOrder tpoWordOrder) {
        return tpoAdminService.addTpoWordOrder(tpoDataId, tpoWordOrder);
    }

    @GetMapping("tpo-word-orders/page")
    public Page<TPOWorkOrder> getWordOrders(@RequestParam(required = false, defaultValue = "") String search,
                                            @RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "50") int size) {
        return tpoAdminService.getWordOrders(search, page, size);
    }

    @PostMapping("tpo-word-order")
    public TPOWorkOrder addWordOrder(@RequestBody TPOWorkOrder tpoWordOrder) {
        return tpoAdminService.addWordOrder(tpoWordOrder);
    }

    @PutMapping("/tpo-data/{tpoDataId}/tpo-word-orders/add-many")
    public ApiResponse addManyTpoWordOrder(@PathVariable int tpoDataId, @RequestBody List<TPOWorkOrder> tpoWordOrders) {
        return tpoAdminService.addManyTpoWordOrder(tpoDataId, tpoWordOrders);
    }

    @PutMapping("/tpo-data/{tpoDataId}/previous-state")
    public ApiResponse addTpoPreviousState(@PathVariable int tpoDataId, @RequestBody List<TPOWorkOrder> tpoWordOrders) {
        return tpoAdminService.addTpoPreviousState(tpoDataId, tpoWordOrders);
    }

    @PutMapping("/tpo-data/{tpoDataId}/tpo-word-order/{tpoWordOrderId}")
    public ApiResponse addTpoWordOrderById(@PathVariable int tpoDataId, @PathVariable int tpoWordOrderId) {
        return tpoAdminService.addTpoWordOrderById(tpoDataId, tpoWordOrderId);
    }

    @PutMapping("/tpo-data/{tpoDataId}/failure")
    public TpoFailureState addFailureTpo(@PathVariable int tpoDataId, @RequestBody TpoFailureState tpoFailureState) {
        return tpoAdminService.addTpoForWOFailureTo(tpoFailureState.getWoId(), tpoDataId, tpoFailureState.getTpoFailureId());
    }

    @PutMapping("/tpo-data/{tpoDataId}/failure/{tpoFailureStateId}")
    public TpoFailureState updateFailureTpo(@PathVariable int tpoDataId,
                                        @PathVariable int tpoFailureStateId,
                                        @RequestBody TpoFailureState tpoFailureState) {
        return tpoAdminService.updateFailureTpo(tpoFailureStateId, tpoFailureState.getWoId(), tpoDataId, tpoFailureState.getTpoFailureId());
    }

    @DeleteMapping("/tpo-data/{tpoDataId}/failure/{tpoFailureStateId}")
    public ApiResponse deleteFailureTpo(@PathVariable int tpoDataId,
                                        @PathVariable int tpoFailureStateId) {
        return tpoAdminService.deleteFailureTpo(tpoFailureStateId, tpoDataId);
    }


    @GetMapping("/tpo-word-orders")
    public List<TPOWorkOrder> getAllTpoWordOrders() {
        return tpoAdminService.getAllTpoWordOrders();
    }

    @DeleteMapping("/tpo-data/{tpoDataId}/tpo-word-order/{tpoWordOrderId}")
    public ApiResponse removeTpoWordOrder(@PathVariable int tpoDataId, @PathVariable int tpoWordOrderId) {
        return tpoAdminService.removeTpoWordOrder(tpoDataId, tpoWordOrderId);
    }

    @PutMapping("/tpo-word-order/{tpoWordOrderId}")
    public TPOWorkOrder updateTpoWordOrder(@PathVariable int tpoWordOrderId, @RequestBody TPOWorkOrder tpoWordOrder) {
        return tpoAdminService.updateTpoWordOrder(tpoWordOrderId, tpoWordOrder);
    }

    @DeleteMapping("/tpo-word-order/{tpoWordOrderId}")
    public ApiResponse deleteTpoWordOrder(@PathVariable int tpoWordOrderId) {
        return tpoAdminService.deleteTpoWordOrder(tpoWordOrderId);
    }


//    @Deprecated
//    @PutMapping("/tpo-word-order/{tpoWordOrderId}/failure")
//    public TPOWorkOrder addTpoWordOrderFailureToWK(@PathVariable int tpoWordOrderId, @RequestBody TPOWorkOrder tpoWordOrder) {
//        return tpoAdminService.addTpoWordOrderFailureToWK(tpoWordOrderId, tpoWordOrder);
//    }

   /* @PutMapping("/tpo-word-order/{tpoWordOrderId}/failures")
    public ApiResponse addTpoWordOrderFailureToWK(@PathVariable int tpoWordOrderId, @RequestBody List<TPOWorkOrder> tpoWordOrders) {
        return tpoAdminService.addTpoWordOrdersFailureToWK(tpoWordOrderId, tpoWordOrders);
    }*/


    @GetMapping("/constant-config")
    public List<ConstantConfig> getAllConstantConfig() {
        return tpoAdminService.getAllConstantConfig();
    }

    @PostMapping("/constant-config")
    public ConstantConfig createConstantConfig(@RequestBody ConstantConfig constantConfig) {
        return tpoAdminService.createConstantConfig(constantConfig);
    }

    @PutMapping("/constant-config/{id}")
    public ConstantConfig updateConstantConfig(@PathVariable int id, @RequestBody ConstantConfig constantConfig) {
        return tpoAdminService.updateConstantConfig(id, constantConfig);
    }

    @DeleteMapping("/constant-config/{id}")
    public ApiResponse deleteConstantConfig(@PathVariable int id) {
        return tpoAdminService.deleteConstantConfig(id);
    }

}
