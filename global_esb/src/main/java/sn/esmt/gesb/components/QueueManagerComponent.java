package sn.esmt.gesb.components;

import org.springframework.stereotype.Component;
import sn.esmt.gesb.payload.ApiResponse;
import sn.esmt.gesb.payload.QueueItem;
import sn.esmt.gesb.wsdl.*;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class QueueManagerComponent {
    private final ConcurrentLinkedQueue<QueueItem<ActionRequest, ApiResponse>> requestQueue = new ConcurrentLinkedQueue<>();
}
