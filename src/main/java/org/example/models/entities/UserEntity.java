package org.example.models.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.models.enums.RoleEnum;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column
    String name;
    @Column
    String username;
    @Column
    String password;
    @Column
    @Enumerated(value = EnumType.STRING)
    RoleEnum role;
    @Column
    boolean isAccountLocked;

    public UserEntity(String name, String username) {
        this.name = name;
        this.username = username;
    }
}
