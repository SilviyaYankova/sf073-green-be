package org.example.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {
    Long transactionId;
    Long amount;
    String number;
    String ip;
    String region;
    LocalDateTime date;
    String result;
    String feedback;
}
