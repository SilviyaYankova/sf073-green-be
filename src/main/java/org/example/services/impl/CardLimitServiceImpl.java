package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.models.entities.CardLimitEntity;
import org.example.repositories.CardLimitRepository;
import org.example.services.CardLimitService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CardLimitServiceImpl implements CardLimitService {
    private final CardLimitRepository cardLimitRepository;

    @Override
    public CardLimitEntity findByNumber(String number) {
        return cardLimitRepository.findByNumber(number);
    }

    @Override
    public void save(CardLimitEntity card) {
        cardLimitRepository.save(card);
    }
}
