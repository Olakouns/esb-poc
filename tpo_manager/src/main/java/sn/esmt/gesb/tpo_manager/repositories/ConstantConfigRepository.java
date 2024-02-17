package sn.esmt.gesb.tpo_manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.esmt.gesb.tpo_manager.models.ConstantConfig;

import java.util.Optional;

public interface ConstantConfigRepository extends JpaRepository<ConstantConfig, Long> {
    Optional<ConstantConfig> findByKeyName(String key);
}
