package com.example.lawfirm.service;

import com.example.lawfirm.model.DocumentRequest;
import com.example.lawfirm.model.GeneratedDocument;
import com.example.lawfirm.repository.GeneratedDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GeneratedDocumentService {

    private final GeneratedDocumentRepository generatedDocumentRepository;

    public GeneratedDocumentService(GeneratedDocumentRepository generatedDocumentRepository) {
        this.generatedDocumentRepository = generatedDocumentRepository;
    }

    public GeneratedDocument save(GeneratedDocument generatedDocument) {
        return generatedDocumentRepository.save(generatedDocument);
    }

    public List<GeneratedDocument> findAll() {
        return generatedDocumentRepository.findAll();
    }

    public Optional<GeneratedDocument> findById(UUID id) {
        return generatedDocumentRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return generatedDocumentRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        generatedDocumentRepository.deleteById(id);
    }

    public List<GeneratedDocument> findByClientCode(String clientCode) {
        return generatedDocumentRepository.findByClientCode(clientCode);
    }

    public boolean existsByDocumentRequest(DocumentRequest documentRequest) {
        return generatedDocumentRepository.existsByDocumentRequest(documentRequest);
    }
}
