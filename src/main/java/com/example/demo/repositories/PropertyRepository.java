package com.example.demo.repositories;

import com.example.demo.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PropertyRepository  extends JpaRepository<Property, UUID> {
    @Query("SELECT p FROM Property p WHERE p.id = :id AND (p.managerId = :userId OR p.ownerId = :userId)")
    List<Property> findUserWithValidRightsbyUserId(
            @Param("id") UUID id,
            @Param("userId") UUID userId
    );
}
