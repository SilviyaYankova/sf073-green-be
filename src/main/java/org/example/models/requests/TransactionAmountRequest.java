package org.example.models.requests;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionAmountRequest {
    @NotNull
    @Min(1L)
    Long amount;
    @NotNull
    String ip;
    @NotNull
    String number;
    @NotNull
    String region;
    @NotNull
    LocalDateTime date;
}
