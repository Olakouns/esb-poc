package sn.esmt.gesb.components;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import sn.esmt.gesb.payload.ApiResponse;
import sn.esmt.gesb.payload.QueueItem;
//import sn.esmt.gesb.wsdl.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class QueueManagerComponent {

//    private final int MAX_QUEUE_SIZE = 5;
//    private final ConcurrentLinkedQueue<QueueItem<ActionRequest, ApiResponse>> requestQueue = new ConcurrentLinkedQueue<>();
//    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
//
//    public void enqueue(ActionRequest actionRequest, DeferredResult<ApiResponse> deferredResult) {
//
//        if (requestQueue.size() >= MAX_QUEUE_SIZE) {
//            deferredResult.setResult(new ApiResponse(false, "The queue is full. Veuillez réessayer dans 5 minutes."));
//        } else {
//            QueueItem<ActionRequest, ApiResponse> queueItem = new QueueItem<>(actionRequest, deferredResult);
//            requestQueue.add(queueItem);
//        }
//    }
//
//    @PostConstruct
//    private void init() {
//        executorService.execute(this::processQueue);
//    }
//
//    private void processQueue() {
//        while (!Thread.interrupted()) {
//            if (!requestQueue.isEmpty()) {
//                QueueItem<ActionRequest, ApiResponse> queueItem = requestQueue.poll();
//                processRequest(queueItem.getData(), queueItem.getKDeferredResult());
//            }
//        }
//    }
//
//    private void processRequest(ActionRequest request, DeferredResult<ApiResponse> deferredResult) {
//        deferredResult.setResult(new ApiResponse(true, "Queue is completed"));
//    }
//
//    @PreDestroy
//    private void destroy() {
//        executorService.shutdown();
//        try {
//            if (!executorService.awaitTermination(120, TimeUnit.SECONDS)) {
//                executorService.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
}
