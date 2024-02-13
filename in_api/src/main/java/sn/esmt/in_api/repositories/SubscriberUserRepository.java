package sn.esmt.in_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.esmt.in_api.models.SubscriberUser;

import java.util.Optional;
import java.util.UUID;

public interface SubscriberUserRepository extends JpaRepository<SubscriberUser, UUID> {
    Optional<SubscriberUser> findByPhoneNumberOrImsi(String phoneNumber, String imsi);
    Optional<SubscriberUser> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
