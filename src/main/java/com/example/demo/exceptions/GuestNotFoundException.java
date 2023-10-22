package com.example.demo.exceptions;

public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException(String message) { super(message);
    }
}
