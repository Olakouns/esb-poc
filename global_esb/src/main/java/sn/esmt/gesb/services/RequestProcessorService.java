package sn.esmt.gesb.services;

import sn.esmt.gesb.wsdl.ActionRequest;
import sn.esmt.models.TPOData;

public interface RequestProcessorService {
    TPOData processRequest(ActionRequest actionRequest);
}
