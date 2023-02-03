package org.example.services;


import org.example.models.entities.CardLimitEntity;

public interface CardLimitService {

    CardLimitEntity findByNumber(String number);

    void save(CardLimitEntity card);
}
