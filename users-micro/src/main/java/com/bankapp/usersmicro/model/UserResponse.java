package com.bankapp.usersmicro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Data
@SuperBuilder
public class UserResponse {
    private Integer userId;
    private String name;
    private String email;
    private String message;
}
