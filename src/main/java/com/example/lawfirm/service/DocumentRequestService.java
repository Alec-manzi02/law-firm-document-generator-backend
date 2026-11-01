package com.example.lawfirm.service;

import com.example.lawfirm.model.DocumentRequest;
import com.example.lawfirm.model.RequestStatus;
import com.example.lawfirm.repository.DocumentRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentRequestService {

    private final DocumentRequestRepository documentRequestRepository;

    public DocumentRequestService(DocumentRequestRepository documentRequestRepository) {
        this.documentRequestRepository = documentRequestRepository;
    }

    public DocumentRequest save(DocumentRequest documentRequest) {
        return documentRequestRepository.save(documentRequest);
    }

    public List<DocumentRequest> findAll() {
        return documentRequestRepository.findAll();
    }

    public Page<DocumentRequest> findAll(Pageable pageable) {
        return documentRequestRepository.findAll(pageable);
    }

    public Optional<DocumentRequest> findById(UUID id) {
        return documentRequestRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return documentRequestRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        documentRequestRepository.deleteById(id);
    }

    public List<DocumentRequest> findByStatus(RequestStatus status) {
        return documentRequestRepository.findByStatusOrderByRequestDateAsc(status);
    }

    public List<DocumentRequest> findBetween(LocalDate start, LocalDate end) {
        return documentRequestRepository.findByRequestDateBetweenOrderByRequestDateAsc(start, end);
    }

    public boolean existsByAttorneyAndDateAndNotVoided(UUID attorneyId, LocalDate requestDate) {
        return documentRequestRepository.existsByAttorneyIdAndRequestDateAndStatusNot(
                attorneyId, requestDate, RequestStatus.VOIDED);
    }

    public List<Object[]> getStatsByStatus() {
        return documentRequestRepository.getStatsByStatus();
    }

    public int cancelDay(UUID attorneyId, LocalDate date) {
        return documentRequestRepository.cancelDayForAttorney(attorneyId, date);
    }
}
