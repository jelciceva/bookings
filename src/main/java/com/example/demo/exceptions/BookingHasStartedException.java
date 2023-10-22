package com.example.demo.exceptions;

public class BookingHasStartedException extends RuntimeException {
    public BookingHasStartedException(String message) { super(message);
    }
}
