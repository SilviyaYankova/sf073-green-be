package org.example.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "suspicious_IPs")
public class SuspiciousIPEntity {
    @Id
    @GeneratedValue()
    Long id;
    @Column
    String ip;
}
