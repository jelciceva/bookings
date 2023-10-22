package com.example.demo.exceptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler(OverlappingBookingException.class)
    public ResponseEntity<String> handleOverlappingBooking(OverlappingBookingException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<String> handleInvalidDate(InvalidDateException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(GuestNotFoundException.class)
    public ResponseEntity<String> handleGuestNotFound(GuestNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<String> handleInvalidDate(PropertyNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<String> handleInvalidDate(BookingNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(BookingHasStartedException.class)
    public ResponseEntity<String> handleInvalidDate(BookingHasStartedException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
