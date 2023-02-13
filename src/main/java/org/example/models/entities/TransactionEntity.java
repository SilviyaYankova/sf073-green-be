package org.example.models.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.models.enums.RegionEnum;



import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions_history")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column
    Long amount;
    @Column
    String number;
    @Column
    String ip;
    @Column
    @Enumerated(value = EnumType.STRING)
    RegionEnum region;
    @Column(name = "created_at")
    LocalDateTime date;
    @Column
    String result;
    @Column
    String feedback;
}
