package com.example.demo;

import com.example.demo.entities.Block;
import com.example.demo.entities.Booking;
import com.example.demo.entities.Property;
import com.example.demo.entities.Users;
import com.example.demo.exceptions.GuestNotFoundException;
import com.example.demo.exceptions.InvalidDateException;
import com.example.demo.exceptions.OverlappingBookingException;
import com.example.demo.exceptions.PropertyNotFoundException;
import com.example.demo.repositories.BlockRepository;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.PropertyRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.BookingService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CreateBookingTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateBooking() {
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(1));
        booking.setGuestId(UUID.randomUUID());
        booking.setPropertyId(UUID.randomUUID());


        when(userRepository.findById(booking.getGuestId())).thenReturn(Optional.of(new Users()));
        when(propertyRepository.findById(booking.getPropertyId())).thenReturn(Optional.of(new Property()));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking createdBooking = bookingService.createBooking(booking);

        assertEquals(booking, createdBooking);
    }
    @Test
    public void testCreateBookingWithInvalidGuest() {
        Booking booking = new Booking();
        booking.setGuestId(UUID.randomUUID());

        when(userRepository.findById(booking.getGuestId())).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> bookingService.createBooking(booking));
    }

    @Test
    public void testCreateBookingWithInvalidProperty() {
        Booking booking = new Booking();
        booking.setPropertyId(UUID.randomUUID());

        when(userRepository.findById(booking.getGuestId())).thenReturn(Optional.of(new Users()));
        when(propertyRepository.findById(booking.getPropertyId())).thenReturn(Optional.empty());

        assertThrows(PropertyNotFoundException.class, () -> bookingService.createBooking(booking));
    }

    @Test
    public void testCreateBookingWithInvalidDates() {
        Booking booking = new Booking();
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().minusDays(1)); //


        when(userRepository.findById(booking.getGuestId())).thenReturn(Optional.of(new Users()));
        when(propertyRepository.findById(booking.getPropertyId())).thenReturn(Optional.of(new Property()));

        try {
            bookingService.createBooking(booking);
            fail("Exception should be thrown for invalid date range.");
        } catch (InvalidDateException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateBookingWithOverlappingBlocks() {
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(1));
        booking.setGuestId(UUID.randomUUID());
        booking.setPropertyId(UUID.randomUUID());


        when(userRepository.findById(booking.getGuestId())).thenReturn(Optional.of(new Users()));
        when(propertyRepository.findById(booking.getPropertyId())).thenReturn(Optional.of(new Property()));


        List<Block> overlappingBlocks = new ArrayList<>();
        overlappingBlocks.add(new Block());
        when(blockRepository.findOverlappingBlocks(booking.getStartDate(), booking.getEndDate(), booking.getPropertyId()))
                .thenReturn(overlappingBlocks);

        try {
            bookingService.createBooking(booking);
            fail("Exception should be thrown for overlapping blocks.");
        } catch (OverlappingBookingException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateBookingWithOverlappingBookings() {
        Booking booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setStartDate(LocalDate.now());
        booking.setEndDate(LocalDate.now().plusDays(1));
        booking.setGuestId(UUID.randomUUID());
        booking.setPropertyId(UUID.randomUUID());


        when(userRepository.findById(booking.getGuestId())).thenReturn(Optional.of(new Users()));
        when(propertyRepository.findById(booking.getPropertyId())).thenReturn(Optional.of(new Property()));


        List<Booking> overlappingBlocks = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setId(UUID.randomUUID());
        overlappingBlocks.add(booking1);
        when(bookingRepository.findOverlappingBookings(booking.getStartDate(), booking.getEndDate(), booking.getPropertyId()))
                .thenReturn(overlappingBlocks);

        try {
            bookingService.createBooking(booking);
            fail("Exception should be thrown for overlapping bookings.");
        } catch (OverlappingBookingException e) {
            // Expected exception
        }
    }


}

