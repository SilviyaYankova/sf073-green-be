package org.example.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAllUsersResponse {
    Long id;
    String name;
    String username;
    String role;
    boolean unLocked;
}
