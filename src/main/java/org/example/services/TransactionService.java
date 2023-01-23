package org.example.services;


import org.example.models.requests.TransactionAmountRequest;
import org.example.models.responses.TransactionResponse;

public interface TransactionService {
    TransactionResponse transaction(TransactionAmountRequest request);
}
