package org.example.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.models.requests.HistoryRequest;
import org.example.models.requests.TransactionAmountRequest;
import org.example.models.responses.HistoryResponse;
import org.example.models.responses.TransactionResponse;
import org.example.services.TransactionService;
import org.example.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequestMapping("/api/antifraud/")
@RequiredArgsConstructor
@RestController
public class TransactionController {
    final TransactionService transactionService;
    final UserService userService;

    @PreAuthorize("hasRole('MERCHANT')")
    @PostMapping("/transaction")
    TransactionResponse transaction(@RequestBody @Valid TransactionAmountRequest request) {
        return transactionService.transaction(request);
    }

    @PreAuthorize("hasRole('SUPPORT')")
    @PutMapping("/transaction")
    HistoryResponse history(@RequestBody @Valid HistoryRequest request) {
        return transactionService.feedback(request);
    }

    @PreAuthorize("hasRole('SUPPORT')")
    @GetMapping("/history")
    List<HistoryResponse> getHistory() {
        return transactionService.history();
    }

    @PreAuthorize("hasRole('SUPPORT')")
    @GetMapping("/history/{number}")
    List<HistoryResponse> getTransaction(@PathVariable String number) {
        return transactionService.getTransaction(number);
    }
}
