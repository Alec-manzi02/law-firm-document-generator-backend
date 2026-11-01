package com.example.lawfirm.controller;

import com.example.lawfirm.model.Chamber;
import com.example.lawfirm.service.ChamberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/chambers")
public class ChamberController {

    private final ChamberService chamberService;

    public ChamberController(ChamberService chamberService) {
        this.chamberService = chamberService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Chamber chamber) {
        chamberService.save(chamber);
        return ResponseEntity.status(HttpStatus.CREATED).body("Chamber saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Chamber>> getAll() {
        return ResponseEntity.ok(chamberService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Chamber> optional = chamberService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The chamber with that id does not exist");
        }
        return ResponseEntity.ok(optional.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Chamber chamber) {
        Optional<Chamber> optional = chamberService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The chamber with that id does not exist");
        }
        Chamber current = optional.get();
        current.setChamberNumber(chamber.getChamberNumber());
        current.setName(chamber.getName());
        Chamber updated = chamberService.save(current);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Optional<Chamber> optional = chamberService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The chamber with that id does not exist");
        }
        chamberService.deleteById(id);
        return ResponseEntity.ok("Chamber deleted successfully");
    }

    @GetMapping("/free")
    public ResponseEntity<List<Chamber>> getFreeChambers() {
        return ResponseEntity.ok(chamberService.findFreeChambers());
    }
}
