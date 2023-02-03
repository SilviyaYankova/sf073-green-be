package org.example.services;


import org.example.models.requests.HistoryRequest;
import org.example.models.requests.TransactionAmountRequest;
import org.example.models.responses.HistoryResponse;
import org.example.models.responses.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse transaction(TransactionAmountRequest request);

    List<HistoryResponse> history( );

    List<HistoryResponse> getTransaction(String number);

    HistoryResponse feedback(HistoryRequest request);
}
