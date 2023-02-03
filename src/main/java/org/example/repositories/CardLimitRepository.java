package org.example.repositories;

import org.example.models.entities.CardLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardLimitRepository extends JpaRepository<CardLimitEntity, Long> {
    CardLimitEntity findByNumber(String number);

}
