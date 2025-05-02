package com.example.spring.Exceptions;

public class UserNotFoundException extends RuntimeException {


    public UserNotFoundException(String message) {
        super(message);
    }
}
