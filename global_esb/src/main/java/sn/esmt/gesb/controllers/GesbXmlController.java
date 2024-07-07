package sn.esmt.gesb.controllers;


import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.soap.SoapFaultException;
import sn.esmt.gesb.components.QueueManagerComponent;
import sn.esmt.gesb.soam.EsbParameter;
import sn.esmt.gesb.soam.EsbRootActionRequest;
import sn.esmt.gesb.soam.EsbRootActionResponse;
import sn.esmt.gesb.soam.EsbService;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/request/gesb/xml")
@RequiredArgsConstructor
@Slf4j
public class GesbXmlController {

    private final QueueManagerComponent queueManagerComponent;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<EsbRootActionResponse> processRequest(@RequestHeader(value = "callback_url", required = false) String callbackURL, @RequestBody EsbRootActionRequest esbRootActionRequest) throws ExecutionException, InterruptedException {
        EsbRootActionResponse response = new EsbRootActionResponse();
        response.setSuccess(false);

        if (StringUtils.isEmpty(esbRootActionRequest.getRequestId())) {
            response.setMessage("Request ID is required");
            return ResponseEntity.badRequest().body(response);
        }

        if (StringUtils.isEmpty(callbackURL)) {
            response.setMessage("Callback URL is required");
            return ResponseEntity.badRequest().body(response);
        }

        if (esbRootActionRequest.getEsbContent() == null) {
            response.setMessage("EsbContent is required");
            return ResponseEntity.badRequest().body(response);
        }

        if (StringUtils.isEmpty(esbRootActionRequest.getEsbContent().getVerb())) {
            response.setMessage("Verb is required");
            return ResponseEntity.badRequest().body(response);
        }

        if (esbRootActionRequest.getEsbContent().getEsbParameter().size() == 0) {
            response.setMessage("minimum one esbParameter is required");
            return ResponseEntity.badRequest().body(response);
        }

        for (EsbParameter esbParameter : esbRootActionRequest.getEsbContent().getEsbParameter()) {
            if (StringUtils.isEmpty(esbParameter.getName())) {
                response.setMessage("esbParameter name is required");
                return ResponseEntity.badRequest().body(response);
            }
        }

        if (esbRootActionRequest.getEsbContent().getEsbServices() != null && !esbRootActionRequest.getEsbContent().getEsbServices().getEsbService().isEmpty()) {
            for (EsbService esbService : esbRootActionRequest.getEsbContent().getEsbServices().getEsbService()) {
                if (esbService.getVerb() == null) {
                    response.setMessage("esbService Verb is required");
                    return ResponseEntity.badRequest().body(response);
                }

                if (esbService.getEsbParameter().isEmpty()) {
                    response.setMessage("minimum one esbParameter is required for esbService");
                    return ResponseEntity.badRequest().body(response);
                }

                for (EsbParameter esbParameter : esbService.getEsbParameter()) {
                    if (StringUtils.isEmpty(esbParameter.getName())) {
                        response.setMessage("esbParameter name is required for esbService");
                        return ResponseEntity.badRequest().body(response);
                    }
                }
            }
        }


        if (queueManagerComponent.getQueueSize() >= queueManagerComponent.MAX_QUEUE_SIZE) {
            response.setMessage("Queue is full");
            return ResponseEntity.unprocessableEntity().body(response);
        }

        log.info("Get request {} at {}", esbRootActionRequest.getRequestId(), new Date().getTime());
        CompletableFuture<EsbRootActionResponse> future = queueManagerComponent.enqueue(esbRootActionRequest);
        EsbRootActionResponse result = future.get();  // Wait for the response
//        queueManagerComponent.enqueue(esbRootActionRequest);
//        response.setSuccess(true);
//        response.setMessage("Request received");
        if (result.isSuccess()){
            return ResponseEntity.ok().body(result);
        } else{
            return ResponseEntity.badRequest().body(result);
        }
    }


}
