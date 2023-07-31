package com.bankapp.usersmicro.model;

public record UserDTO (Integer userId,
                       String firstName,
                       String lastName,
                       String email,
                       String password,
                       String phoneNumber,
                       Character userType){}
