package com.bankapp.usersmicro.exceptions;

public class NonExistentUserException extends RuntimeException{
    public NonExistentUserException(String message){super(message);}
}
