package sn.esmt.gesb.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import sn.esmt.gesb.wsdl.ActionRequest;
import sn.esmt.gesb.wsdl.ActionResponse;

import java.util.concurrent.ForkJoinPool;

@RestController
@RequestMapping("queue")
public class QueueManagerController {

    @GetMapping("/async-deferredresult")
    public DeferredResult<ResponseEntity<?>> handleReqDefResult() {
        System.err.println("Received async-deferredresult request");
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        ForkJoinPool.commonPool().submit(() -> {
            System.err.println("Processing in separate thread");
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
            }
            output.setResult(ResponseEntity.ok(new ActionRequest()));
        });

        System.err.println("servlet thread freed");
        return output;
    }
}
