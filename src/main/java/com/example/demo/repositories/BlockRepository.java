package com.example.demo.repositories;

import com.example.demo.entities.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BlockRepository extends JpaRepository<Block, UUID> {
    @Query("SELECT b FROM Block b WHERE b.startDate < :newBookingEndDate AND b.endDate > :newBookingStartDate AND b.propertyId = :propertyId")
    List<Block> findOverlappingBlocks(
            @Param("newBookingStartDate") LocalDate newBookingStartDate,
            @Param("newBookingEndDate") LocalDate newBookingEndDate,
            @Param("propertyId") UUID propertyId

    );
}
