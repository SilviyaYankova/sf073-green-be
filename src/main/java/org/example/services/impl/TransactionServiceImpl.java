package org.example.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.models.entities.CardLimitEntity;
import org.example.models.entities.TransactionEntity;
import org.example.models.entities.UserEntity;
import org.example.models.enums.TransactionEnum;
import org.example.models.requests.HistoryRequest;
import org.example.models.requests.TransactionAmountRequest;
import org.example.models.responses.HistoryResponse;
import org.example.models.responses.TransactionResponse;
import org.example.repositories.TransactionRepository;
import org.example.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.models.enums.TransactionEnum.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
    private static final long MAX_ALLOWED_AMOUNT = 200;
    private static final long MANUAL_PROCESSING_AMOUNT = 1500;
    final ModelMapper modelMapper;
    final Validator validator;
    final TransactionRepository transactionRepository;
    final UserService userService;
    final SuspiciousIPService SuspiciousIPService;
    final StolenCardService stolenCardService;
    final CardLimitService cardLimitService;

    @Transactional
    @Override
    public TransactionResponse transaction(TransactionAmountRequest request) {
        Optional<UserEntity> loggedUser = userService.getLoggedUser();
        if (!loggedUser.get().isAccountLocked()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        CardLimitEntity cardEntity = cardLimitService.findByNumber(request.getNumber());
        if (cardEntity == null) {
            CardLimitEntity card = modelMapper.map(request, CardLimitEntity.class);
            card.setAllowed(MAX_ALLOWED_AMOUNT);
            card.setManual(MANUAL_PROCESSING_AMOUNT);
            cardLimitService.save(card);
        }

        TransactionEntity transaction = modelMapper.map(request, TransactionEntity.class);
        transactionRepository.save(transaction);
        return getResponse(request, transaction);
    }

    public TransactionResponse getResponse(TransactionAmountRequest request, TransactionEntity transaction) {
        CardLimitEntity card = cardLimitService.findByNumber(request.getNumber());
        List<TransactionEntity> byNumber = transactionRepository.findByNumberAndDateBetween(
                transaction.getNumber(),
                transaction.getDate().minusHours(1), transaction.getDate());

        long uniqueIPsCount = byNumber.stream().map(TransactionEntity::getIp).distinct().count();
        long uniqueRegionsCount = byNumber.stream().map(TransactionEntity::getRegion).distinct().count();

        TransactionEnum ipCorrelation = checkCountOf(uniqueIPsCount);
        TransactionEnum regionCorrelation = checkCountOf(uniqueRegionsCount);
        TransactionEnum stolen = checkCard(transaction.getNumber());
        TransactionEnum ip = checkIp(transaction.getIp());
        TransactionEnum amount = checkAmount(request.getAmount(), card);

        List<String> list = new ArrayList<>(List.of(ipCorrelation.name(),
                                                    regionCorrelation.name(),
                                                    stolen.name(),
                                                    ip.name(),
                                                    amount.name()));
        String error = list.stream().sorted(Comparator.reverseOrder()).limit(1).findFirst().get();
        List<String> info = getInfo(ipCorrelation, regionCorrelation, stolen, ip, amount, error);
        transaction.setFeedback("");
        boolean allEqual = list.stream().allMatch(list.get(0)::equals);
        if (allEqual) {
            transaction.setResult(ALLOWED.name());
            return new TransactionResponse(ALLOWED.name(), "none");
        } else {
            transaction.setResult(error);
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
        if (SuspiciousIPService.ipIsBlacklisted(ip)) {
            ipEnum = PROHIBITED;
        } else {
            ipEnum = ALLOWED;
        }
        return ipEnum;
    }

    private TransactionEnum checkCard(String number) {
        TransactionEnum stolen;
        if (stolenCardService.cardIsStolen(number)) {
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

    private TransactionEnum checkAmount(long amount, CardLimitEntity card) {
        long allowed = card.getAllowed();
        long manual = card.getManual();
        if (amount <= allowed) {
            return ALLOWED;
        } else if (amount <= manual) {
            return MANUAL_PROCESSING;
        }
        return PROHIBITED;
    }

    @Override
    public List<HistoryResponse> history() {
        return transactionRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(TransactionEntity::getId))
                .map(a -> modelMapper.map(a, HistoryResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoryResponse> getTransaction(String number) {
        validator.validateCardNumber(number);
        if (transactionRepository.findByNumber(number).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return transactionRepository.findByNumber(number)
                                    .stream()
                                    .map(a -> modelMapper.map(a, HistoryResponse.class))
                                    .collect(Collectors.toList());
    }

    @Override
    public HistoryResponse feedback(HistoryRequest request) {
        Optional<TransactionEntity> entity = transactionRepository.findById(request.getTransactionId());
        if (entity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        TransactionEntity transaction = entity.get();
        String result = transaction.getResult();
        String feedback = request.getFeedback();
        checkExceptions(transaction, result, feedback);
        setNewCardLimit(request, transaction, result, feedback);
        return modelMapper.map(entity, HistoryResponse.class);
    }

    private static void checkExceptions(TransactionEntity transaction, String result, String feedback) {
        if (result.equals(feedback)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (!feedback.equals(ALLOWED.name())
                && !feedback.equals(MANUAL_PROCESSING.name())
                && !feedback.equals(PROHIBITED.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!transaction.getFeedback().equals("")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    private void setNewCardLimit(HistoryRequest request, TransactionEntity transaction, String result, String feedback) {
        if (result.equals(ALLOWED.name()) && feedback.equals(MANUAL_PROCESSING.name())) {
            decreaseMaxAllowed(request, transaction);
        } else if (result.equals(ALLOWED.name()) && feedback.equals(PROHIBITED.name())) {
            decreaseMaxAllowed(request, transaction);
            decreaseMaxManual(request, transaction);
        } else if (result.equals(MANUAL_PROCESSING.name()) && feedback.equals(ALLOWED.name())) {
            increaseMaxAllowed(request, transaction);
        } else if (result.equals(MANUAL_PROCESSING.name()) && feedback.equals(PROHIBITED.name())) {
            decreaseMaxManual(request, transaction);
        } else if (result.equals(PROHIBITED.name()) && feedback.equals(ALLOWED.name())) {
            increaseMaxAllowed(request, transaction);
            increaseMaxManual(request, transaction);
        } else if (result.equals(PROHIBITED.name()) && feedback.equals(MANUAL_PROCESSING.name())) {
            increaseMaxManual(request, transaction);
        }
    }

    private void increaseMaxManual(HistoryRequest request, TransactionEntity transaction) {
        CardLimitEntity card = cardLimitService.findByNumber(transaction.getNumber());
        double increasedMaxManualLimit = 0.8 * card.getManual() + 0.2 * transaction.getAmount();
        long newLimit = (long) Math.ceil(increasedMaxManualLimit);
        card.setManual(newLimit);
        cardLimitService.save(card);
        transaction.setFeedback(request.getFeedback());
        transactionRepository.save(transaction);
    }

    private void increaseMaxAllowed(HistoryRequest request, TransactionEntity transaction) {
        CardLimitEntity card = cardLimitService.findByNumber(transaction.getNumber());
        double increasedMaxAllowedLimit = 0.8 * card.getAllowed() + 0.2 * transaction.getAmount();
        long newLimit = (long) Math.ceil(increasedMaxAllowedLimit);
        card.setAllowed(newLimit);
        cardLimitService.save(card);
        transaction.setFeedback(request.getFeedback());
        transactionRepository.save(transaction);
    }

    void decreaseMaxManual(HistoryRequest request, TransactionEntity transaction) {
        CardLimitEntity card = cardLimitService.findByNumber(transaction.getNumber());
        double decreasedMaxManualLimit = 0.8 * card.getManual() - 0.2 * transaction.getAmount();
        long newLimit = (long) Math.ceil(decreasedMaxManualLimit);
        card.setManual(newLimit);
        cardLimitService.save(card);
        transaction.setFeedback(request.getFeedback());
        transactionRepository.save(transaction);
    }

    void decreaseMaxAllowed(HistoryRequest request, TransactionEntity transaction) {
        CardLimitEntity card = cardLimitService.findByNumber(transaction.getNumber());
        double decreasedMaxAllowedLimit = 0.8 * card.getAllowed() - 0.2 * transaction.getAmount();
        long newLimit = (long) Math.ceil(decreasedMaxAllowedLimit);
        card.setAllowed(newLimit);
        cardLimitService.save(card);
        transaction.setFeedback(request.getFeedback());
        transactionRepository.save(transaction);
    }
}
