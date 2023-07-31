package com.bankapp.accountsmicro.exceptions;

public class InvalidAccountException extends RuntimeException{
    public InvalidAccountException(String message){
        super(message);
    }
}
