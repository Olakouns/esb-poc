package sn.esmt.hlr_api.services;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.esmt.hlr_api.exceptions.NotFoundException;
import sn.esmt.hlr_api.exceptions.RequestNotAcceptableException;
import sn.esmt.hlr_api.models.SubscriberType;
import sn.esmt.hlr_api.models.SubscriberUser;
import sn.esmt.hlr_api.models.TLService;
import sn.esmt.hlr_api.repositories.SubscriberUserRepository;
import sn.esmt.hlr_api.repositories.TLServiceRepository;
import sn.esmt.hlr_api.soam.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriberUserServiceImpl implements SubscriberUserService {

    private final SubscriberUserRepository subscriberUserRepository;
    private final TLServiceRepository tlServiceRepository;

    private final EntityManager entityManager;

    @Override
    public ApiResponse activateSubscriber(SubscriberData subscriberData) {

        if (subscriberUserRepository.existsByPhoneNumberOrImsi(subscriberData.getPhoneNumber(), subscriberData.getImsi())) {
            throw new RequestNotAcceptableException("User with " + subscriberData.getPhoneNumber() + " or " + subscriberData.getImsi() + " already exist.");
        }

        SubscriberUser subscriberUser = SubscriberUser
                .builder()
                .name(subscriberData.getName())
                .phoneNumber(subscriberData.getPhoneNumber())
                .imsi(subscriberData.getImsi())
                .subscriberType(SubscriberType.valueOf(subscriberData.getSubscriberType()))
                .tlServices(new ArrayList<>())
                .createdAt(new Date())
                .build();

        subscriberUserRepository.save(subscriberUser);
        return new ApiResponse(true, "User created");
    }

    @Override
    public ApiResponse deactivateSubscriber(DeactivateSubscriberRequest deactivateSubscriberRequest) {
        Optional<SubscriberUser> optionalSubscriberUser = subscriberUserRepository.findByPhoneNumber(deactivateSubscriberRequest.getPhoneNumber());
        if (optionalSubscriberUser.isPresent()) {
            subscriberUserRepository.delete(optionalSubscriberUser.get());
            return new ApiResponse(true, "User with phone  = " + deactivateSubscriberRequest.getPhoneNumber() + " deactivated");
        } else {
            throw new NotFoundException("DeactivateSubscriber", "PhoneNumber", deactivateSubscriberRequest.getPhoneNumber());
        }
    }

    @Override
    public SubscriberData displaySubscriber(DisplaySubscriberRequest displaySubscriberRequest) {
        Optional<SubscriberUser> optionalSubscriberUser = subscriberUserRepository.findByPhoneNumber(displaySubscriberRequest.getPhoneNumber());
        if (optionalSubscriberUser.isPresent()) {
            SubscriberUser user = optionalSubscriberUser.get();
            entityManager.detach(user);
            List<TLService> tlServiceList = new ArrayList<>();
            for (TLService tlService : optionalSubscriberUser.get().getTlServices()) {
                if (tlService.isActivated()) {
                    tlServiceList.add(tlService);
                }
            }
            user.setTlServices(tlServiceList);
            return subscriberDataMapper(user, tlServiceList);
        } else {
            throw new NotFoundException("DisplaySubscriber", "PhoneNumber", displaySubscriberRequest.getPhoneNumber());
        }
    }

    @Override
    public ApiResponse modifyServiceSubscriber(ModifyServiceSubscriberRequest modifyServiceSubscriberRequest) {
        Optional<SubscriberUser> optionalSubscriberUser = subscriberUserRepository.findFirstByPhoneNumber(modifyServiceSubscriberRequest.getPhoneNumber());
        if (optionalSubscriberUser.isEmpty()) {
            throw new NotFoundException("ModifyServiceSubscriber", "PhoneNumber", modifyServiceSubscriberRequest.getPhoneNumber());
        }

        SubscriberUser user = optionalSubscriberUser.get();

        sn.esmt.hlr_api.soam.Service service = modifyServiceSubscriberRequest.getService();
        switch (modifyServiceSubscriberRequest.getVerb()) {
            case ADD:
                user.getTlServices().add(TLService.builder()
                        .activated(service.isActive())
                        .serviceType(sn.esmt.hlr_api.models.ServiceType.valueOf(service.getServiceType().name()))
                        .targetNumber(service.getTargetNumber())
                        .build());
                break;
            case UPDATE:
                break;
            case DELETE:
                break;
        }
        return null;
    }

    private SubscriberData subscriberDataMapper(SubscriberUser user, List<TLService> tlServiceList) {
        SubscriberData subscriberData = new SubscriberData();
        subscriberData.setName(user.getName());
        subscriberData.setImsi(user.getImsi());
        subscriberData.setPhoneNumber(user.getPhoneNumber());
        Services services = new Services();

        for (TLService tlService : tlServiceList) {
            sn.esmt.hlr_api.soam.Service service = new sn.esmt.hlr_api.soam.Service();
            service.setServiceType(ServiceType.valueOf(tlService.getServiceType().name()));
            service.setTargetNumber(tlService.getTargetNumber());
            service.setActive(tlService.isActivated());
            services.getService().add(service);
        }
        subscriberData.setServices(services);
        return subscriberData;
    }
}
