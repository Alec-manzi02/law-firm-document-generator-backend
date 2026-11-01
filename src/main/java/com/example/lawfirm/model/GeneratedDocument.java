package com.example.lawfirm.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "generated_document")
public class GeneratedDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String documentType;

    private String notes;

    private int pageCount;

    private LocalDate generatedDate;

    @ManyToOne
    @JoinColumn(name = "document_request_id")
    private DocumentRequest documentRequest;

    public GeneratedDocument() {
    }

    public GeneratedDocument(UUID id, String documentType, String notes, int pageCount, LocalDate generatedDate, DocumentRequest documentRequest) {
        this.id = id;
        this.documentType = documentType;
        this.notes = notes;
        this.pageCount = pageCount;
        this.generatedDate = generatedDate;
        this.documentRequest = documentRequest;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDate generatedDate) {
        this.generatedDate = generatedDate;
    }

    public DocumentRequest getDocumentRequest() {
        return documentRequest;
    }

    public void setDocumentRequest(DocumentRequest documentRequest) {
        this.documentRequest = documentRequest;
    }
}
