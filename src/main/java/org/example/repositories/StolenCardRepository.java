package org.example.repositories;

import org.example.models.entities.StolenCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StolenCardRepository extends JpaRepository<StolenCardEntity, Long> {
    Optional<StolenCardEntity> findByNumber(String number);
}
