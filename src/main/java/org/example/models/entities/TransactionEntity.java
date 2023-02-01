package org.example.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.models.enums.RegionEnum;
import org.example.models.enums.TransactionEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions_history")
public class TransactionEntity {
    @Id
    @GeneratedValue
    Long id;
    @Column
    Long amount;
    @Column
    String ip;
    @Column
    String number;
    @Column
    @Enumerated(value = EnumType.STRING)
    RegionEnum region;
    @Column
    LocalDateTime date;
}
