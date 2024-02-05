package sn.esmt.gesb.tpo_manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sn.esmt.gesb.tpo_manager.models.TPOData;

public interface TPODataRepository extends JpaRepository<TPOData, Integer>, JpaSpecificationExecutor<TPOData> {
}
