package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.models.entities.TransactionEntity;
import org.example.models.entities.UserEntity;
import org.example.models.enums.TransactionEnum;
import org.example.models.requests.TransactionAmountRequest;
import org.example.models.responses.TransactionResponse;
import org.example.repositories.TransactionRepository;
import org.example.services.CardService;
import org.example.services.IPService;
import org.example.services.TransactionService;
import org.example.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.example.models.enums.TransactionEnum.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    final UserService userService;
    final IPService IPService;
    final CardService cardService;
    final ModelMapper modelMapper;
    final TransactionRepository transactionRepository;

    @Override
    public TransactionResponse transaction(TransactionAmountRequest request) {
        Optional<UserEntity> loggedUser = userService.getLoggedUser();
        if (!loggedUser.get().isAccountLocked()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        TransactionEntity transaction = modelMapper.map(request, TransactionEntity.class);
        transactionRepository.save(transaction);
        return getResponse(request, transaction);
    }

    private TransactionResponse getResponse(TransactionAmountRequest request, TransactionEntity transaction) {
        List<TransactionEntity> byNumber = transactionRepository.findByNumberAndDateBetween(
                transaction.getNumber(),
                transaction.getDate().minusHours(1), transaction.getDate());

        long uniqueIPsCount = byNumber.stream().map(TransactionEntity::getIp).distinct().count();
        long uniqueRegionsCount = byNumber.stream().map(TransactionEntity::getRegion).distinct().count();

        TransactionEnum ipCorrelation = checkCountOf(uniqueIPsCount);
        TransactionEnum regionCorrelation = checkCountOf(uniqueRegionsCount);
        TransactionEnum stolen = checkCard(transaction.getNumber());
        TransactionEnum ip = checkIp(transaction.getIp());
        TransactionEnum amount = checkAmount(request.getAmount());

        List<String> list = new ArrayList<>(List.of(ipCorrelation.name(),
                                                    regionCorrelation.name(),
                                                    stolen.name(),
                                                    ip.name(),
                                                    amount.name()));
        String error = list.stream().sorted(Comparator.reverseOrder()).limit(1).findFirst().get();

        List<String> info = getInfo(ipCorrelation, regionCorrelation, stolen, ip, amount, error);

        boolean allEqual = list.stream().allMatch(list.get(0)::equals);
        if (allEqual) {
            return new TransactionResponse("ALLOWED", "none");
        } else {
            String join = String.join(", ", info);
            return new TransactionResponse(error, join);
        }
    }

    private static List<String> getInfo(TransactionEnum ipCorrelation, TransactionEnum regionCorrelation, TransactionEnum stolen, TransactionEnum ip, TransactionEnum amount, String error) {
        List<String> info = new ArrayList<>();
        if (error.equals(amount.name())) {
            info.add("amount");
        }
        if (error.equals(stolen.name())) {
            info.add("card-number");
        }
        if (error.equals(ip.name())) {
            info.add("ip");
        }
        if (error.equals(ipCorrelation.name())) {
            info.add("ip-correlation");
        }
        if (error.equals(regionCorrelation.name())) {
            info.add("region-correlation");
        }
        return info;
    }

    private TransactionEnum checkIp(String ip) {
        TransactionEnum ipEnum;
        if (IPService.ipIsBlacklisted(ip)) {
            ipEnum = PROHIBITED;
        } else {
            ipEnum = ALLOWED;
        }
        return ipEnum;
    }

    private TransactionEnum checkCard(String number) {
        TransactionEnum stolen;
        if (cardService.cardIsStolen(number)) {
            stolen = PROHIBITED;
        } else {
            stolen = ALLOWED;
        }
        return stolen;
    }

    private TransactionEnum checkCountOf(long request) {
        if (request <= 2) {
            return ALLOWED;
        } else if (request == 3) {
            return MANUAL_PROCESSING;
        }
        return PROHIBITED;
    }

    private TransactionEnum checkAmount(long amount) {
        if (amount <= 200) {
            return ALLOWED;
        } else if (amount <= 1500) {
            return MANUAL_PROCESSING;
        }
        return PROHIBITED;
    }
}