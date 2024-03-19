package sn.esmt.gesb.components;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import sn.esmt.gesb.services.Impl.RequestProcessor;
import sn.esmt.gesb.soam.EsbRootActionRequest;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueManagerComponent {

    private final RequestProcessor coreService;

    @Value("${max.queue.size}")
    public int MAX_QUEUE_SIZE;
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
                    log.info("Start request {} treatment  at {}", queueItem.getRequestId(), new Date());
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
