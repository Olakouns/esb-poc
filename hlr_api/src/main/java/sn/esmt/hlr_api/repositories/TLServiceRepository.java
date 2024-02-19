package sn.esmt.hlr_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.esmt.hlr_api.models.TLService;

public interface TLServiceRepository extends JpaRepository<TLService, Long> {
}