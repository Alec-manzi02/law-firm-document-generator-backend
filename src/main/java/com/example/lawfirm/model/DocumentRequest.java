package com.example.lawfirm.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "document_request")
public class DocumentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDate requestDate;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String templateName;

    private String jurisdiction;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "attorney_id")
    private Attorney attorney;

    public DocumentRequest() {
    }

    public DocumentRequest(UUID id, LocalDate requestDate, RequestStatus status, String templateName, String jurisdiction, Client client, Attorney attorney) {
        this.id = id;
        this.requestDate = requestDate;
        this.status = status;
        this.templateName = templateName;
        this.jurisdiction = jurisdiction;
        this.client = client;
        this.attorney = attorney;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Attorney getAttorney() {
        return attorney;
    }

    public void setAttorney(Attorney attorney) {
        this.attorney = attorney;
    }
}
