package com.example.demo.controllers;

import com.example.demo.entities.Block;
import com.example.demo.services.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/blocks")
public class BlockController {

    @Autowired
    private BlockService blockService;

    // Create a new block
    @PostMapping
    public ResponseEntity<Block> createBlock(@RequestBody Block block) {
        Block createdBlock = blockService.createBlock(block);
        return new ResponseEntity<>(createdBlock, HttpStatus.CREATED);
    }

    // Get all blocks
    @GetMapping
    public ResponseEntity<List<Block>> getAllBlocks() {
        List<Block> blocks = blockService.getAllBlocks();
        return new ResponseEntity<>(blocks, HttpStatus.OK);
    }

    // Get a block by ID
    @GetMapping("/{id}")
    public ResponseEntity<Block> getBlockById(@PathVariable UUID id) {
        Block block = blockService.getBlockById(id);
        if (block != null) {
            return new ResponseEntity<>(block, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update a block
    @PutMapping("/{id}")
    public ResponseEntity<Block> updateBlock(@PathVariable UUID id, @RequestBody Block block) {
        Block updatedBlock = blockService.updateBlock(id, block);
        return new ResponseEntity<>(updatedBlock, HttpStatus.OK);

    }

    // Delete a block
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable UUID id) {
        blockService.deleteBlock(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
