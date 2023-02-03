package org.example.models.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class HistoryRequest {
    @NotNull
    Long transactionId;
    @NotNull
    String feedback;
}
