package sn.esmt.gesb.components;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import sn.esmt.gesb.services.Impl.RequestProcessor;
import sn.esmt.gesb.soam.EsbRootActionRequest;
//import sn.esmt.gesb.wsdl.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueManagerComponent {

    private final RequestProcessor coreService;

    public final int MAX_QUEUE_SIZE = 5;
    private final ConcurrentLinkedQueue<EsbRootActionRequest> requestQueue = new ConcurrentLinkedQueue<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Async
    public void enqueue(EsbRootActionRequest actionRequest) {
        requestQueue.add(actionRequest);
    }

    @PostConstruct
    private void init() {
        log.info("QueueManagerComponent started");
        executorService.execute(this::processQueue);
    }

    private void processQueue() {
        while (!Thread.interrupted()) {
            if (!requestQueue.isEmpty()) {
                EsbRootActionRequest queueItem = requestQueue.poll();
                if (queueItem != null) {
                    processRequest(queueItem);
                }
            }
        }
    }

    private void processRequest(EsbRootActionRequest request) {
        coreService.processRequest(request);
    }

    public int getQueueSize() {
        return requestQueue.size();
    }

    @PreDestroy
    private void destroy() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(120, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
