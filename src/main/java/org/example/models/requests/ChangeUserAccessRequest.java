package org.example.models.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class ChangeUserAccessRequest {
    String username;
    String operation;
}
