package com.example.demo.controllers;


import com.example.demo.entities.Booking;
import com.example.demo.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    // Implement create, read, update, and delete operations
    @Autowired
    private BookingService bookingService;

    // Create a new booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        System.out.println(booking.getStartDate());
        System.out.println(booking.getEndDate());
        Booking createdBooking = bookingService.createBooking(booking);

            return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);

    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    // Get a booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable UUID id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking != null) {
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update a booking
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable UUID id, @RequestBody Booking booking) {
        Booking updatedBooking = bookingService.updateBooking(id, booking);

            return new ResponseEntity<>(updatedBooking, HttpStatus.OK);

    }

    // Rebook a booking
    @PostMapping("/rebook/{id}")
    public ResponseEntity<Booking> rebookBooking(@PathVariable UUID id, @RequestBody Booking booking) {
        Booking updatedBooking = bookingService.rebookBooking(id, booking);
            return new ResponseEntity<>(updatedBooking, HttpStatus.OK);

    }

    // Soft delete a booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID id) {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}