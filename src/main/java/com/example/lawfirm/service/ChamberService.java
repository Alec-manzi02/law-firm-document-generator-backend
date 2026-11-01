package com.example.lawfirm.service;

import com.example.lawfirm.model.Chamber;
import com.example.lawfirm.repository.ChamberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChamberService {

    private final ChamberRepository chamberRepository;

    public ChamberService(ChamberRepository chamberRepository) {
        this.chamberRepository = chamberRepository;
    }

    public Chamber save(Chamber chamber) {
        return chamberRepository.save(chamber);
    }

    public List<Chamber> findAll() {
        return chamberRepository.findAll();
    }

    public Optional<Chamber> findById(UUID id) {
        return chamberRepository.findById(id);
    }

    public Optional<Chamber> findByChamberNumber(int chamberNumber) {
        return chamberRepository.findByChamberNumber(chamberNumber);
    }

    public boolean existsById(UUID id) {
        return chamberRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        chamberRepository.deleteById(id);
    }

    public List<Chamber> findFreeChambers() {
        return chamberRepository.findFreeChambers();
    }
}
