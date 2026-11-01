package com.example.lawfirm.controller;

import com.example.lawfirm.model.DocumentRequest;
import com.example.lawfirm.model.RequestStatus;
import com.example.lawfirm.service.DocumentRequestService;
import com.example.lawfirm.service.GeneratedDocumentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/document-requests")
public class DocumentRequestController {

    private final DocumentRequestService documentRequestService;
    private final GeneratedDocumentService generatedDocumentService;

    public DocumentRequestController(DocumentRequestService documentRequestService,
                                     GeneratedDocumentService generatedDocumentService) {
        this.documentRequestService = documentRequestService;
        this.generatedDocumentService = generatedDocumentService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody DocumentRequest documentRequest) {
        if (documentRequest.getRequestDate() != null && documentRequest.getRequestDate().isBefore(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Document request date cannot be in the past");
        }

        if (documentRequest.getAttorney() != null && documentRequest.getAttorney().getId() != null) {
            boolean conflict = documentRequestService.existsByAttorneyAndDateAndNotVoided(
                    documentRequest.getAttorney().getId(), documentRequest.getRequestDate());
            if (conflict) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Attorney already has a document request on that date");
            }
        }

        documentRequest.setStatus(RequestStatus.DRAFTED);
        documentRequestService.save(documentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Document request saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<DocumentRequest>> getAll() {
        return ResponseEntity.ok(documentRequestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<DocumentRequest> optional = documentRequestService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The document request with that id does not exist");
        }
        return ResponseEntity.ok(optional.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody DocumentRequest documentRequest) {
        Optional<DocumentRequest> optional = documentRequestService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The document request with that id does not exist");
        }
        DocumentRequest current = optional.get();
        current.setRequestDate(documentRequest.getRequestDate());
        current.setStatus(documentRequest.getStatus());
        current.setTemplateName(documentRequest.getTemplateName());
        current.setJurisdiction(documentRequest.getJurisdiction());
        if (documentRequest.getClient() != null) {
            current.setClient(documentRequest.getClient());
        }
        if (documentRequest.getAttorney() != null) {
            current.setAttorney(documentRequest.getAttorney());
        }
        DocumentRequest updated = documentRequestService.save(current);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Optional<DocumentRequest> optional = documentRequestService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The document request with that id does not exist");
        }
        if (generatedDocumentService.existsByDocumentRequest(optional.get())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("That document request has generated documents, delete them first");
        }
        documentRequestService.deleteById(id);
        return ResponseEntity.ok("Document request deleted successfully");
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<DocumentRequest>> getByStatus(@RequestParam RequestStatus status) {
        return ResponseEntity.ok(documentRequestService.findByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<List<DocumentRequest>> getBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ResponseEntity.ok(documentRequestService.findBetween(startDate, endDate));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable UUID id, @RequestParam RequestStatus status) {
        Optional<DocumentRequest> optional = documentRequestService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The document request with that id does not exist");
        }
        DocumentRequest request = optional.get();
        RequestStatus currentStatus = request.getStatus();

        boolean allowed = false;
        if (currentStatus == RequestStatus.DRAFTED) {
            if (status == RequestStatus.REVIEWED || status == RequestStatus.VOIDED) {
                allowed = true;
            }
        } else if (currentStatus == RequestStatus.REVIEWED) {
            if (status == RequestStatus.EXECUTED || status == RequestStatus.VOIDED) {
                allowed = true;
            }
        }

        if (!allowed) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot change status from " + currentStatus + " to " + status);
        }

        request.setStatus(status);
        documentRequestService.save(request);
        return ResponseEntity.ok("Document request status updated successfully");
    }

    @GetMapping("/stats/by-status")
    public ResponseEntity<List<Object[]>> getStatsByStatus() {
        return ResponseEntity.ok(documentRequestService.getStatsByStatus());
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID attorneyId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        int count = documentRequestService.cancelDay(attorneyId, localDate);
        return ResponseEntity.ok(count + " document requests cancelled");
    }

    @GetMapping("/page")
    public ResponseEntity<Page<DocumentRequest>> getPage(Pageable pageable) {
        return ResponseEntity.ok(documentRequestService.findAll(pageable));
    }
}
