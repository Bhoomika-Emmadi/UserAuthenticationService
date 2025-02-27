package com.example.userauthenticationservice.exceptions;

public class UserDoesntExistException extends RuntimeException{
    public UserDoesntExistException(String message){
        super(message);
    }
}
