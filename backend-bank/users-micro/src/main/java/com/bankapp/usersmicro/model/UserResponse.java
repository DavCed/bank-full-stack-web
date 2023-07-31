package com.bankapp.usersmicro.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class UserResponse {
    private Integer userId;
    private String name;
    private String email;
    private Character userType;
    private String message;
}
