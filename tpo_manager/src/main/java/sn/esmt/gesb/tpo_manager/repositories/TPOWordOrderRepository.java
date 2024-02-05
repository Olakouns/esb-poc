package sn.esmt.gesb.tpo_manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sn.esmt.gesb.tpo_manager.models.TPOWordOrder;

public interface TPOWordOrderRepository extends JpaRepository<TPOWordOrder, Integer>, JpaSpecificationExecutor<TPOWordOrder> {
}
