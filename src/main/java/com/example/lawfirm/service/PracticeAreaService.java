package com.example.lawfirm.service;

import com.example.lawfirm.model.PracticeArea;
import com.example.lawfirm.repository.PracticeAreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PracticeAreaService {

    private final PracticeAreaRepository practiceAreaRepository;

    public PracticeAreaService(PracticeAreaRepository practiceAreaRepository) {
        this.practiceAreaRepository = practiceAreaRepository;
    }

    public PracticeArea save(PracticeArea practiceArea) {
        return practiceAreaRepository.save(practiceArea);
    }

    public List<PracticeArea> findAll() {
        return practiceAreaRepository.findAll();
    }

    public Optional<PracticeArea> findById(UUID id) {
        return practiceAreaRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return practiceAreaRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        practiceAreaRepository.deleteById(id);
    }

    public List<PracticeArea> findUnusedPracticeAreas() {
        return practiceAreaRepository.findUnusedPracticeAreas();
    }
}
