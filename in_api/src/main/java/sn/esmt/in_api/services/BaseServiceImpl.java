package sn.esmt.in_api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.esmt.in_api.models.SubscriberUser;
import sn.esmt.in_api.repositories.SubscriberUserRepository;
import sn.esmt.in_api.soam.ApiResponse;
import sn.esmt.in_api.soam.NewConnectionRequest;
import sn.esmt.in_api.soam.RechargingRequest;
import sn.esmt.in_api.soam.TerminationRequest;

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
            return new ApiResponse(false, "PhoneNumber : " + subscriberUser.getPhoneNumber() + " already exist");
        }

        subscriberUserRepository.save(subscriberUser);
        return new ApiResponse(true, "User created");
    }

    @Override
    public ApiResponse termination(TerminationRequest terminationRequest) {
        Optional<SubscriberUser> userOptional = subscriberUserRepository.findByPhoneNumber(terminationRequest.getPhoneNumber());
        if (userOptional.isEmpty()) {
            return new ApiResponse(false, "PhoneNumber : " + terminationRequest.getPhoneNumber() + " doesn't exist");
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
            return new ApiResponse(false, "PhoneNumber : " + rechargingRequest.getPhoneNumber() + " doesn't exist");
        }

        SubscriberUser subscriberUser = userOptional.get();
        subscriberUser.setCallBalance(rechargingRequest.getCallBalance());
        subscriberUser.setSmsBalance(rechargingRequest.getSmsBalance());
        subscriberUser.setDataBalance(rechargingRequest.getDataBalance());
        subscriberUserRepository.save(subscriberUser);
        return new ApiResponse(true, "User with phone " + rechargingRequest.getPhoneNumber() + "recharged");
    }


}
