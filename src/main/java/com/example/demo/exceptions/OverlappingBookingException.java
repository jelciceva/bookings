package com.example.demo.exceptions;

public class OverlappingBookingException extends RuntimeException {
    public OverlappingBookingException(String message) { super(message);
    }
}