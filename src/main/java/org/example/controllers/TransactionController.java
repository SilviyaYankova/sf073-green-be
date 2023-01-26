package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.models.entities.UserEntity;
import org.example.models.requests.TransactionAmountRequest;
import org.example.models.responses.TransactionResponse;
import org.example.services.TransactionService;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TransactionController {
    final TransactionService transactionService;
    final UserService userService;

    @PreAuthorize("hasRole('MERCHANT')")
    @PostMapping("/api/antifraud/transaction")
    TransactionResponse transaction(@RequestBody @Valid TransactionAmountRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<UserEntity> userEntity = userService.findUserByUsername(username);
        if (!userEntity.get().isAccountLocked()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return transactionService.transaction(request);
    }
}
