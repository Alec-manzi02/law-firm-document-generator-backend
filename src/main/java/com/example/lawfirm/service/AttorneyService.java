package com.example.lawfirm.service;

import com.example.lawfirm.model.Attorney;
import com.example.lawfirm.model.Chamber;
import com.example.lawfirm.repository.AttorneyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttorneyService {

    private final AttorneyRepository attorneyRepository;

    public AttorneyService(AttorneyRepository attorneyRepository) {
        this.attorneyRepository = attorneyRepository;
    }

    public Attorney save(Attorney attorney) {
        return attorneyRepository.save(attorney);
    }

    public List<Attorney> findAll() {
        return attorneyRepository.findAll();
    }

    public Optional<Attorney> findById(UUID id) {
        return attorneyRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return attorneyRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        attorneyRepository.deleteById(id);
    }

    public List<Attorney> findByPracticeAreaName(String name) {
        return attorneyRepository.findByPracticeAreaName(name);
    }

    public boolean existsByChamber(Chamber chamber) {
        return attorneyRepository.existsByChamber(chamber);
    }
}
