package com.example.demo.repositories;

import com.example.demo.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("SELECT b FROM Booking b WHERE b.startDate < :newBookingEndDate AND b.endDate > :newBookingStartDate AND b.propertyId = :propertyId and b.active = true ")
    List<Booking> findOverlappingBookings(
            @Param("newBookingStartDate") LocalDate newBookingStartDate,
            @Param("newBookingEndDate") LocalDate newBookingEndDate,
            @Param("propertyId") UUID propertyId
    );
}
