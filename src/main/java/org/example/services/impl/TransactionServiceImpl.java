package org.example.services.impl;
import lombok.RequiredArgsConstructor;
import org.example.models.entities.TransactionEntity;
import org.example.models.enums.TransactionEnum;
import org.example.models.requests.TransactionAmountRequest;
import org.example.models.responses.TransactionResponse;
import org.example.services.TransactionService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    public TransactionResponse transaction(TransactionAmountRequest request) {
        TransactionEntity transactionEntity = getTransactionResult(request);
        return new TransactionResponse(transactionEntity.getTransactionEnum().toString());
    }

    private TransactionEntity getTransactionResult(TransactionAmountRequest request) {
        Long amount = request.getAmount();
        if (amount <= 200L) {
            return new TransactionEntity(TransactionEnum.ALLOWED);
        } else if (amount <= 1500L) {
            return new TransactionEntity(TransactionEnum.MANUAL_PROCESSING);
        }
        return new TransactionEntity(TransactionEnum.PROHIBITED);
    }
}
