package sn.esmt.gesb.services.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.esmt.gesb.components.QueueManagerComponent;
import sn.esmt.gesb.services.RequestProcessorService;
import sn.esmt.gesb.wsdl.ActionRequest;
//import sn.esmt.models.TPOData;


@Service
@RequiredArgsConstructor
public class RequestProcessorServiceImpl implements RequestProcessorService {

    @Override
//    public TPOData processRequest(ActionRequest actionRequest) {
    public void processRequest(ActionRequest actionRequest) {

    }
}
