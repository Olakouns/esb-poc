package sn.esmt.in_api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.esmt.in_api.exceptions.CustomSoapException;
import sn.esmt.in_api.models.SubscriberUser;
import sn.esmt.in_api.repositories.SubscriberUserRepository;
import sn.esmt.in_api.soam.*;

import java.util.Date;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BaseServiceImpl implements BaseService {
    private final SubscriberUserRepository subscriberUserRepository;

    @Override
    public ApiResponse newConnection(NewConnectionRequest newConnectionRequest) {
        SubscriberUser subscriberUser = SubscriberUser
                .builder()
                .imsi(newConnectionRequest.getImsi())
                .name(newConnectionRequest.getName())
                .phoneNumber(newConnectionRequest.getPhoneNumber())
                .createdAt(new Date())
                .build();

        if (subscriberUserRepository.existsByPhoneNumber(subscriberUser.getPhoneNumber())) {
            throw new CustomSoapException("NewConnectionRequest", "PhoneNumber", subscriberUser.getPhoneNumber());
        }

        subscriberUserRepository.save(subscriberUser);
        return new ApiResponse(true, "User created");
    }

    @Override
    public ApiResponse termination(TerminationRequest terminationRequest) {
        Optional<SubscriberUser> userOptional = subscriberUserRepository.findByPhoneNumber(terminationRequest.getPhoneNumber());
        if (userOptional.isEmpty()) {
            throw new CustomSoapException("SubscriberUser", "PhoneNumber", terminationRequest.getPhoneNumber());
        }
        subscriberUserRepository.delete(userOptional.get());
        return new ApiResponse(true, "Termination done");
    }

    @Override
    public ApiResponse query(String number) {
        return null;
    }

    @Override
    public ApiResponse recharging(RechargingRequest rechargingRequest) {
        Optional<SubscriberUser> userOptional = subscriberUserRepository.findByPhoneNumber(rechargingRequest.getPhoneNumber());
        if (userOptional.isEmpty()) {
            throw new CustomSoapException("SubscriberUser", "PhoneNumber", rechargingRequest.getPhoneNumber());
        }

        SubscriberUser subscriberUser = userOptional.get();
        subscriberUser.setCallBalance(rechargingRequest.getCallBalance());
        subscriberUser.setSmsBalance(rechargingRequest.getSmsBalance());
        subscriberUser.setDataBalance(rechargingRequest.getDataBalance());
        subscriberUserRepository.save(subscriberUser);
        return new ApiResponse(true, "User with phone " + rechargingRequest.getPhoneNumber() + "recharged");
    }

    @Override
    public DisplaySubscriberResponse displaySubscriber(DisplaySubscriberRequest displaySubscriberRequest) {
        Optional<SubscriberUser> userOptional = subscriberUserRepository.findByPhoneNumber(displaySubscriberRequest.getPhoneNumber());
        if (userOptional.isEmpty()) {
            throw new CustomSoapException("SubscriberUser", "PhoneNumber", displaySubscriberRequest.getPhoneNumber());
        }

        DisplaySubscriberResponse displaySubscriberResponse = new DisplaySubscriberResponse();
        displaySubscriberResponse.setImsi(userOptional.get().getImsi());
        displaySubscriberResponse.setSubscriberName(userOptional.get().getName());
        displaySubscriberResponse.setSmsBalance(userOptional.get().getSmsBalance());
        displaySubscriberResponse.setDataBalance(userOptional.get().getDataBalance());
        displaySubscriberResponse.setCallBalance(userOptional.get().getCallBalance());
        displaySubscriberResponse.setPhoneNumber(userOptional.get().getPhoneNumber());
        return displaySubscriberResponse;
    }


}
