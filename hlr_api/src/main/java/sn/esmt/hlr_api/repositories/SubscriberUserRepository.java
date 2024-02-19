package sn.esmt.hlr_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.esmt.hlr_api.models.SubscriberUser;

import java.util.Optional;
import java.util.UUID;

public interface SubscriberUserRepository extends JpaRepository<SubscriberUser, UUID> {
    Optional<SubscriberUser> findByPhoneNumber(String phoneNumber);

    Optional<SubscriberUser> findFirstByPhoneNumberOrImsi(String number, String imsi);
    Optional<SubscriberUser> findFirstByPhoneNumber(String number);

    boolean existsByPhoneNumberOrImsi(String phoneNumber, String imsi);
}
