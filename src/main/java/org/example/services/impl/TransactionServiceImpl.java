package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.models.entities.UserEntity;
import org.example.models.enums.TransactionEnum;
import org.example.models.requests.TransactionAmountRequest;
import org.example.models.responses.TransactionResponse;
import org.example.services.CardService;
import org.example.services.IPService;
import org.example.services.TransactionService;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    final UserService userService;
    final IPService IPService;
    final CardService cardService;

    @Override
    public TransactionResponse transaction(TransactionAmountRequest request) {
        Optional<UserEntity> loggedUser = userService.getLoggedUser();
        if (!loggedUser.get().isAccountLocked()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        List<String> info = new ArrayList<>();
        createResponse(request, transactionResponse, info);
        String join = String.join(", ", info);
        transactionResponse.setInfo(join);
        return transactionResponse;
    }

    private void createResponse(TransactionAmountRequest request, TransactionResponse transactionResponse, List<String> info) {
        Long amount = request.getAmount();
        if (amount <= 200L) {
            transactionResponse.setResult(TransactionEnum.ALLOWED.name());
            info.add("none");
        } else if (amount <= 1500L) {
            transactionResponse.setResult(TransactionEnum.MANUAL_PROCESSING.name());
            info.add("amount");
        } else {
            transactionResponse.setResult(TransactionEnum.PROHIBITED.name());
            info.add("amount");
        }

        if (cardService.cardIsBlacklisted(request.getNumber())) {
            transactionResponse.setResult(TransactionEnum.PROHIBITED.name());
            info.add("card-number");
        }
        if (IPService.ipIsBlacklisted(request.getIp())) {
            transactionResponse.setResult(TransactionEnum.PROHIBITED.name());
            info.add("ip");
        }

        if (info.contains("ip") || info.contains("card-number")) {
            if (amount <= 1500L) {
                info.remove("amount");
            }
        }
    }
}