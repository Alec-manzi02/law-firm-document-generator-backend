package com.example.lawfirm.controller;

import com.example.lawfirm.model.PracticeArea;
import com.example.lawfirm.service.PracticeAreaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/practice-areas")
public class PracticeAreaController {

    private final PracticeAreaService practiceAreaService;

    public PracticeAreaController(PracticeAreaService practiceAreaService) {
        this.practiceAreaService = practiceAreaService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody PracticeArea practiceArea) {
        practiceAreaService.save(practiceArea);
        return ResponseEntity.status(HttpStatus.CREATED).body("Practice area saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<PracticeArea>> getAll() {
        return ResponseEntity.ok(practiceAreaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<PracticeArea> optional = practiceAreaService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The practice area with that id does not exist");
        }
        return ResponseEntity.ok(optional.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody PracticeArea practiceArea) {
        Optional<PracticeArea> optional = practiceAreaService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The practice area with that id does not exist");
        }
        PracticeArea current = optional.get();
        current.setName(practiceArea.getName());
        PracticeArea updated = practiceAreaService.save(current);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Optional<PracticeArea> optional = practiceAreaService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The practice area with that id does not exist");
        }
        practiceAreaService.deleteById(id);
        return ResponseEntity.ok("Practice area deleted successfully");
    }

    @GetMapping("/unused")
    public ResponseEntity<List<PracticeArea>> getUnusedPracticeAreas() {
        return ResponseEntity.ok(practiceAreaService.findUnusedPracticeAreas());
    }
}
