package sn.esmt.gesb.tpo_manager.repositories.chain;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.esmt.gesb.tpo_manager.models.chain.ListNode;

public interface ListNodeRepository extends JpaRepository<ListNode, Integer> {
}
