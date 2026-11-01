package com.example.lawfirm.controller;

import com.example.lawfirm.model.Attorney;
import com.example.lawfirm.model.Chamber;
import com.example.lawfirm.model.PracticeArea;
import com.example.lawfirm.service.AttorneyService;
import com.example.lawfirm.service.ChamberService;
import com.example.lawfirm.service.PracticeAreaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/attorneys")
public class AttorneyController {

    private final AttorneyService attorneyService;
    private final PracticeAreaService practiceAreaService;
    private final ChamberService chamberService;

    public AttorneyController(AttorneyService attorneyService,
                              PracticeAreaService practiceAreaService,
                              ChamberService chamberService) {
        this.attorneyService = attorneyService;
        this.practiceAreaService = practiceAreaService;
        this.chamberService = chamberService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Attorney attorney) {
        attorneyService.save(attorney);
        return ResponseEntity.status(HttpStatus.CREATED).body("Attorney saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Attorney>> getAll() {
        return ResponseEntity.ok(attorneyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Attorney> optional = attorneyService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The attorney with that id does not exist");
        }
        return ResponseEntity.ok(optional.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Attorney attorney) {
        Optional<Attorney> optional = attorneyService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The attorney with that id does not exist");
        }
        Attorney current = optional.get();
        current.setFirstName(attorney.getFirstName());
        current.setLastName(attorney.getLastName());
        current.setBarNumber(attorney.getBarNumber());
        if (attorney.getChamber() != null) {
            current.setChamber(attorney.getChamber());
        }
        if (attorney.getPracticeAreas() != null) {
            current.setPracticeAreas(attorney.getPracticeAreas());
        }
        Attorney updated = attorneyService.save(current);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Optional<Attorney> optional = attorneyService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The attorney with that id does not exist");
        }
        attorneyService.deleteById(id);
        return ResponseEntity.ok("Attorney deleted successfully");
    }

    @GetMapping("/by-practice-area")
    public ResponseEntity<List<Attorney>> getByPracticeArea(@RequestParam String name) {
        return ResponseEntity.ok(attorneyService.findByPracticeAreaName(name));
    }

    @PutMapping("/{attorneyId}/practice-areas/{practiceAreaId}")
    public ResponseEntity<?> addPracticeArea(@PathVariable UUID attorneyId, @PathVariable UUID practiceAreaId) {
        Optional<Attorney> attorneyOpt = attorneyService.findById(attorneyId);
        if (attorneyOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The attorney with that id does not exist");
        }
        Optional<PracticeArea> practiceAreaOpt = practiceAreaService.findById(practiceAreaId);
        if (practiceAreaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The practice area with that id does not exist");
        }
        Attorney attorney = attorneyOpt.get();
        PracticeArea practiceArea = practiceAreaOpt.get();

        if (attorney.getPracticeAreas() == null) {
            attorney.setPracticeAreas(new ArrayList<>());
        }
        for (PracticeArea pa : attorney.getPracticeAreas()) {
            if (pa.getId() != null && pa.getId().equals(practiceAreaId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Attorney already has this practice area");
            }
        }

        attorney.getPracticeAreas().add(practiceArea);
        attorneyService.save(attorney);
        return ResponseEntity.ok("Practice area added to the attorney");
    }

    @PatchMapping("/{attorneyId}/chamber")
    public ResponseEntity<?> changeChamber(@PathVariable UUID attorneyId, @RequestParam int chamberNumber) {
        Optional<Attorney> attorneyOpt = attorneyService.findById(attorneyId);
        if (attorneyOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The attorney with that id does not exist");
        }
        Optional<Chamber> chamberOpt = chamberService.findByChamberNumber(chamberNumber);
        if (chamberOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The chamber with that number does not exist");
        }
        Chamber targetChamber = chamberOpt.get();
        if (attorneyService.existsByChamber(targetChamber)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("That chamber is taken by another attorney");
        }
        Attorney attorney = attorneyOpt.get();
        attorney.setChamber(targetChamber);
        attorneyService.save(attorney);
        return ResponseEntity.ok("Chamber changed successfully");
    }
}
