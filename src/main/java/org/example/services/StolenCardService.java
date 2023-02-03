package org.example.services;

import org.example.models.responses.CardResponse;
import org.example.models.responses.DeleteCardResponse;

import java.util.List;

public interface StolenCardService {
    CardResponse addCard(String cardNumber);

    boolean cardIsStolen(String number);

    DeleteCardResponse delete(String number);

    List<CardResponse> getAll();
}
