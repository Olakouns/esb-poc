package sn.esmt.hlr_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.CLIENT)
public class RequestNotAcceptableException extends RuntimeException {
    public RequestNotAcceptableException(String message) {
        super(message);
    }
}
