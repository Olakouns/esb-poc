package sn.esmt.gesb.tpo_manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.esmt.models.TPOData;

public interface TPODataRepository extends JpaRepository<TPOData, Integer> {
}
