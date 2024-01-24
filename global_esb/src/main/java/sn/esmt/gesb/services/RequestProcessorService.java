package sn.esmt.gesb.services;

import sn.esmt.gesb.wsdl.ActionRequest;

public interface RequestProcessorService {
    void processRequest(ActionRequest actionRequest);
}
