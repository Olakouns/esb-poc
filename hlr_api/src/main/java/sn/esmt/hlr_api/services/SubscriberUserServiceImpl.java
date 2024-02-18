package sn.esmt.hlr_api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.esmt.hlr_api.soam.*;

@Service
@RequiredArgsConstructor
public class SubscriberUserServiceImpl implements SubscriberUserService {
    @Override
    public ApiResponse activateSubscriber(SubscriberData subscriberData) {
        return null;
    }

    @Override
    public ApiResponse deactivateSubscriber(DeactivateSubscriberRequest deactivateSubscriberRequest) {
        return null;
    }

    @Override
    public SubscriberData displaySubscriber(DisplaySubscriberRequest displaySubscriberRequest) {
        return null;
    }

    @Override
    public ApiResponse modifyServiceSubscriber(ModifyServiceSubscriberRequest modifyServiceSubscriberRequest) {
        return null;
    }
}
