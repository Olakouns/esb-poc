package sn.esmt.hlr_api.services;

import org.springframework.http.ResponseEntity;
import sn.esmt.hlr_api.soam.*;

public interface SubscriberUserService {

    ApiResponse activateSubscriber(SubscriberData subscriberData);

    ApiResponse deactivateSubscriber(DeactivateSubscriberRequest deactivateSubscriberRequest);

    SubscriberData displaySubscriber(DisplaySubscriberRequest displaySubscriberRequest);

    ApiResponse modifyServiceSubscriber(ModifyServiceSubscriberRequest modifyServiceSubscriberRequest);
}
