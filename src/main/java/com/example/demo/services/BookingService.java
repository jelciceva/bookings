package com.example.demo.services;

import com.example.demo.entities.Block;
import com.example.demo.entities.Booking;
import com.example.demo.exceptions.*;
import com.example.demo.repositories.BlockRepository;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.PropertyRepository;
import com.example.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    public Booking createBooking(Booking booking) {

        if(userRepository.findById(booking.getGuestId()).isEmpty()){
            throw new  GuestNotFoundException("Guest not found with id:" + booking.getGuestId());
        }

        if(propertyRepository.findById(booking.getPropertyId()).isEmpty()){
            throw new PropertyNotFoundException("Property not found with id:" + booking.getPropertyId());
        }

        System.out.println(booking.getId());
        validateDates(booking, booking.getId());

        return bookingRepository.save(booking);

    }

    private void validateDates(Booking booking, UUID id){
        if (booking.getStartDate().equals(booking.getEndDate()) || booking.getEndDate().isBefore(booking.getStartDate()) || booking.getEndDate().isBefore(LocalDate.now()) || booking.getStartDate().isBefore(LocalDate.now())) {
            log.error("Invalid date range.");
            throw new InvalidDateException("Invalid date range.");
        }

        List<Block> blocks = blockRepository.findOverlappingBlocks(booking.getStartDate(), booking.getEndDate(), booking.getPropertyId());

        System.out.println(blocks.isEmpty());
        System.out.println(blocks);
        if (!blocks.isEmpty()) {
            log.warn("Booking is not possible for requested dates due to blocks.");

            throw new OverlappingBookingException("Booking is not possible for requested dates due to blocks.");
        }

        List<Booking> bookings = bookingRepository.findOverlappingBookings(booking.getStartDate(), booking.getEndDate(), booking.getPropertyId());


        if (!bookings.isEmpty()) {
            bookings.forEach(b -> {
                if (  !b.getId().equals(id)){
                    log.warn("Booking overlaps with an existing reservation.");
                    throw new OverlappingBookingException("Booking overlaps with an existing reservation.");
                }

            });

        }



    }


    public Booking rebookBooking(UUID id, Booking booking) {

        deleteBooking(id);
        return createBooking(booking);
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get a booking by ID
    public Booking getBookingById(UUID id) {
        return bookingRepository.findById(id).orElse(null);
    }

    // Update a booking
    public Booking updateBooking(UUID id, Booking newBooking) {

        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if(bookingOptional.isEmpty()){
            throw new BookingNotFoundException("Booking not found with ID: " + id);
        }
        validateDates(newBooking,id);

        bookingOptional.get().setStartDate(newBooking.getStartDate());
        bookingOptional.get().setEndDate(newBooking.getEndDate());
        bookingOptional.get().setGuestName(newBooking.getGuestName());
        return bookingRepository.save(bookingOptional.get());

    }

    // Delete a booking
    public void deleteBooking(UUID bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);

        if (bookingOptional.isPresent()) {

            Booking booking = bookingOptional.get();
            if (booking.getStartDate().isBefore(LocalDate.now())) {
                throw new BookingHasStartedException("Cannot delete a booking that has already started or concluded.");
            }

            booking.setActive(false);

            bookingRepository.save(booking);
        } else {
            throw new BookingNotFoundException("Booking not found with ID: " + bookingId);
        }
    }
}