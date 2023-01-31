package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.models.requests.TransactionAmountRequest;
import org.example.models.responses.TransactionResponse;
import org.example.services.TransactionService;
import org.example.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TransactionController {
    final TransactionService transactionService;
    final UserService userService;

    @PreAuthorize("hasRole('MERCHANT')")
    @PostMapping("/api/antifraud/transaction")
    TransactionResponse transaction(@RequestBody @Valid TransactionAmountRequest request) {
        return transactionService.transaction(request);
    }
}
