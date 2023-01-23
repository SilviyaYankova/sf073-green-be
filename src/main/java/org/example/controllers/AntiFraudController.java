package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.models.requests.TransactionAmountRequest;
import org.example.models.responses.TransactionResponse;
import org.example.services.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AntiFraudController {
    final TransactionService transactionService;

    @PostMapping("/api/antifraud/transaction")
    TransactionResponse transaction(@RequestBody @Valid TransactionAmountRequest request) {
        return transactionService.transaction(request);
    }
}
