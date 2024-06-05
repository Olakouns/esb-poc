package sn.esmt.gesb.tpo_manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.esmt.gesb.tpo_manager.models.TpoFailureState;

import java.util.Optional;

public interface TpoFailureStateRepository extends JpaRepository<TpoFailureState, Integer> {
    Optional<TpoFailureState> findByTpoIdAndWoId(int tpoId, int woId);
    boolean existsByTpoIdAndWoId(int tpoId, int woId);
}
