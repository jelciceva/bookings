package com.example.demo;

import com.example.demo.entities.Block;
import com.example.demo.entities.Booking;
import com.example.demo.exceptions.BookingNotFoundException;
import com.example.demo.exceptions.InvalidDateException;
import com.example.demo.exceptions.OverlappingBookingException;
import com.example.demo.repositories.BlockRepository;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.services.BookingService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UpdateBookingTest {
    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BlockRepository blockRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateBooking() {

        UUID bookingId = UUID.randomUUID();
        Booking existingBooking = new Booking();
        existingBooking.setId(bookingId);
        existingBooking.setStartDate(LocalDate.of(2023, 10, 23));
        existingBooking.setEndDate(LocalDate.of(2023, 10, 25));
        existingBooking.setGuestName("Test Guest");


        Booking updatedBooking = new Booking();
        updatedBooking.setId(bookingId); // Match the ID for an update
        updatedBooking.setStartDate(LocalDate.of(2023, 11, 1));
        updatedBooking.setEndDate(LocalDate.of(2023, 11, 5));
        updatedBooking.setGuestName("Updated Guest");


        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(bookingRepository.save(existingBooking)).thenReturn(existingBooking);


        Booking result = bookingService.updateBooking(bookingId, updatedBooking);

        // Assertions
        assertNotNull(result);
        assertEquals(bookingId, result.getId());
        assertEquals(updatedBooking.getStartDate(), result.getStartDate());
        assertEquals(updatedBooking.getEndDate(), result.getEndDate());
        assertEquals(updatedBooking.getGuestName(), result.getGuestName());
    }

    @Test
    public void testUpdateNonExistentBooking() {
        UUID nonExistentBookingId = UUID.randomUUID();

        when(bookingRepository.findById(nonExistentBookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.updateBooking(nonExistentBookingId, new Booking());
        });
    }

    @Test
    public void testUpdateBookingWithInvalidDates() {
        UUID bookingId = UUID.randomUUID();
        Booking invalidBooking = new Booking();
        invalidBooking.setId(bookingId);
        invalidBooking.setStartDate(LocalDate.of(2023, 11, 5));
        invalidBooking.setEndDate(LocalDate.of(2023, 11, 1));

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(invalidBooking));

        assertThrows(InvalidDateException.class, () -> {
            bookingService.updateBooking(bookingId, invalidBooking);
        });
    }

    @Test
    public void testUpdateBookingWithOverlappingBlocks() {
        Booking originalBooking = new Booking();
        originalBooking.setId(UUID.fromString("5273848c-9370-470f-b016-671d4139d95b"));
        originalBooking.setStartDate(LocalDate.of(2024, 10, 10));
        originalBooking.setEndDate(LocalDate.of(2024, 10, 15));
        originalBooking.setPropertyId(UUID.randomUUID());

        Block overlappingBlock = new Block();
        overlappingBlock.setId(UUID.randomUUID());
        overlappingBlock.setStartDate(LocalDate.of(2024, 10, 22));
        overlappingBlock.setEndDate(LocalDate.of(2024, 10, 29));
        overlappingBlock.setPropertyId(originalBooking.getPropertyId());

        List<Block> overlappingBlocks = new ArrayList<>();
        overlappingBlocks.add(overlappingBlock);


        when(bookingRepository.findById(UUID.fromString("5273848c-9370-470f-b016-671d4139d95b"))).thenReturn(Optional.of(originalBooking));

        Booking updatedBooking = new Booking();
        updatedBooking.setStartDate(LocalDate.of(2024, 10, 20));
        updatedBooking.setEndDate(LocalDate.of(2024, 10, 25));
        updatedBooking.setGuestName("Updated Guest");
        updatedBooking.setPropertyId(originalBooking.getPropertyId());

        when(blockRepository.findOverlappingBlocks(updatedBooking.getStartDate(), updatedBooking.getEndDate(), updatedBooking.getPropertyId()))
                .thenReturn(overlappingBlocks);


        assertThrows(OverlappingBookingException.class, () -> bookingService.updateBooking(originalBooking.getId(), updatedBooking));


        verify(bookingRepository, never()).save(updatedBooking);
    }

}
