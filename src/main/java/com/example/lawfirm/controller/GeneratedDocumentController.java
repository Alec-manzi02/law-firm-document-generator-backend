package com.example.lawfirm.controller;

import com.example.lawfirm.model.DocumentRequest;
import com.example.lawfirm.model.GeneratedDocument;
import com.example.lawfirm.model.RequestStatus;
import com.example.lawfirm.service.DocumentRequestService;
import com.example.lawfirm.service.GeneratedDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/generated-documents")
public class GeneratedDocumentController {

    private final GeneratedDocumentService generatedDocumentService;
    private final DocumentRequestService documentRequestService;

    public GeneratedDocumentController(GeneratedDocumentService generatedDocumentService,
                                       DocumentRequestService documentRequestService) {
        this.generatedDocumentService = generatedDocumentService;
        this.documentRequestService = documentRequestService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam UUID documentRequestId,
                                  @RequestBody GeneratedDocument generatedDocument) {
        Optional<DocumentRequest> docReqOpt = documentRequestService.findById(documentRequestId);
        if (docReqOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The document request with that id does not exist");
        }

        DocumentRequest docReq = docReqOpt.get();
        if (docReq.getStatus() != RequestStatus.EXECUTED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Generated documents are only for EXECUTED document requests");
        }

        generatedDocument.setDocumentRequest(docReq);
        generatedDocument.setGeneratedDate(LocalDate.now());
        generatedDocumentService.save(generatedDocument);
        return ResponseEntity.status(HttpStatus.CREATED).body("Generated document saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<GeneratedDocument>> getAll() {
        return ResponseEntity.ok(generatedDocumentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<GeneratedDocument> optional = generatedDocumentService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The generated document with that id does not exist");
        }
        return ResponseEntity.ok(optional.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody GeneratedDocument generatedDocument) {
        Optional<GeneratedDocument> optional = generatedDocumentService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The generated document with that id does not exist");
        }
        GeneratedDocument current = optional.get();
        current.setDocumentType(generatedDocument.getDocumentType());
        current.setNotes(generatedDocument.getNotes());
        current.setPageCount(generatedDocument.getPageCount());
        if (generatedDocument.getDocumentRequest() != null) {
            current.setDocumentRequest(generatedDocument.getDocumentRequest());
        }
        GeneratedDocument updated = generatedDocumentService.save(current);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Optional<GeneratedDocument> optional = generatedDocumentService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The generated document with that id does not exist");
        }
        generatedDocumentService.deleteById(id);
        return ResponseEntity.ok("Generated document deleted successfully");
    }

    @GetMapping("/client/{clientCode}")
    public ResponseEntity<List<GeneratedDocument>> getByClientCode(@PathVariable String clientCode) {
        return ResponseEntity.ok(generatedDocumentService.findByClientCode(clientCode));
    }
}
