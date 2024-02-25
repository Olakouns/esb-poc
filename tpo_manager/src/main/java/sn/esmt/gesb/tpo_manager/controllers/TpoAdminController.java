package sn.esmt.gesb.tpo_manager.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import sn.esmt.gesb.dto.ApiResponse;
import sn.esmt.gesb.tpo_manager.models.TPOData;
import sn.esmt.gesb.tpo_manager.models.TPOWorkOrder;
import sn.esmt.gesb.tpo_manager.services.TpoAdminService;

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
    public TPOData createTpoData(TPOData tpoData) {
        return tpoAdminService.createTpoData(tpoData);
    }

    @PutMapping("/tpo-data/{id}")
    public TPOData updateTpoData(@PathVariable int id, @RequestBody TPOData tpoData) {
        return tpoAdminService.updateTpoData(id, tpoData);
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

    @PutMapping("/tpo-data/{tpoDataId}/tpo-word-order/{tpoWordOrderId}")
    public ApiResponse addTpoWordOrderById(@PathVariable int tpoDataId, @PathVariable int tpoWordOrderId) {
        return tpoAdminService.addTpoWordOrderById(tpoDataId, tpoWordOrderId);
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

    @PutMapping("/tpo-word-order/{tpoWordOrderId}/failure")
    public TPOWorkOrder addTpoWordOrderFailureToWK(@PathVariable int tpoWordOrderId, @RequestBody TPOWorkOrder tpoWordOrder) {
        return tpoAdminService.addTpoWordOrderFailureToWK(tpoWordOrderId, tpoWordOrder);
    }

}
