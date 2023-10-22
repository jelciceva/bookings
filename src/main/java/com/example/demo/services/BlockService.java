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
public class BlockService {


    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    public Block createBlock(Block block) {
        if(userRepository.findById(block.getUserId()).isEmpty()){
            throw new  GuestNotFoundException("Guest not found with id:" + block.getUserId());
        }

        if(propertyRepository.findById(block.getPropertyId()).isEmpty()){
            throw new PropertyNotFoundException("Property not found with id:" + block.getPropertyId());
        }

        validateUser(block);
        validateDates(block);
        return blockRepository.save(block);
    }

    public List<Block> getAllBlocks() {
        return blockRepository.findAll();
    }

    public Block getBlockById(UUID id) {
        return blockRepository.findById(id).orElse(null);
    }

    public Block updateBlock(UUID id, Block newBlock) {
        validateUser(newBlock);
        validateDates(newBlock);
        Block existingBlock = blockRepository.findById(id).orElse(null);
        if (existingBlock != null) {
            existingBlock.setStartDate(newBlock.getStartDate());
            existingBlock.setEndDate(newBlock.getEndDate());
            return blockRepository.save(existingBlock);
        }
        return null;
    }

    private void validateUser(Block block){
       if( propertyRepository.findUserWithValidRightsbyUserId(block.getPropertyId(), block.getUserId()).isEmpty()){
           throw new UnauthorizedAccessException("User with ID " + block.getUserId() + " does not have permission to perform this operation on the Block.");

       }
    }

    private void validateDates(Block block){
        if (block.getStartDate().equals(block.getEndDate()) || block.getEndDate().isBefore(block.getStartDate()) || block.getEndDate().isBefore(LocalDate.now()) || block.getStartDate().isBefore(LocalDate.now())) {
            log.error("Invalid date range.");
            throw new InvalidDateException("Invalid date range.");
        }

        List<Booking> bookings = bookingRepository.findOverlappingBookings(block.getStartDate(), block.getEndDate(), block.getPropertyId());

        if (!bookings.isEmpty()) {
            log.warn("Block is not possible for requested dates due to bookings.");
            throw new OverlappingBookingException("Block is not possible for requested dates due to bookings.");
        }


    }

    public void deleteBlock(UUID id) {

        Optional<Block> blockOptional = blockRepository.findById(id);

        if (blockOptional.isPresent()) {
            validateUser(blockOptional.get());
            Block block = blockOptional.get();
            if (block.getStartDate().isBefore(LocalDate.now())) {
                throw new BookingHasStartedException("Cannot delete a block that has already started or concluded.");
            }
            blockRepository.delete(blockOptional.get());

        } else {
            throw new BookingNotFoundException("Block not found with ID: " + id);
        }
    }
}

