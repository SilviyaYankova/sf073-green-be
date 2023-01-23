package org.example.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.models.enums.TransactionEnum;

@Getter
@Setter
@NoArgsConstructor@AllArgsConstructor
public class TransactionEntity {
    TransactionEnum transactionEnum;
}
