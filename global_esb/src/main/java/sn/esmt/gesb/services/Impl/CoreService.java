package sn.esmt.gesb.services.Impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.esmt.gesb.components.QueueManagerComponent;
import sn.esmt.gesb.wsdl.ActionRequest;
import sn.esmt.gesb.wsdl.ActionResponse;

@Service
@RequiredArgsConstructor
public class CoreService {

    private final QueueManagerComponent queueManagerComponent;

    public ActionResponse processRequest(ActionRequest actionRequest) {
        return  new ActionResponse();
    }
}
