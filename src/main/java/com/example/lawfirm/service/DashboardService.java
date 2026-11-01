package com.example.lawfirm.service;

import com.example.lawfirm.model.RequestStatus;
import com.example.lawfirm.repository.AttorneyRepository;
import com.example.lawfirm.repository.ChamberRepository;
import com.example.lawfirm.repository.ClientRepository;
import com.example.lawfirm.repository.DocumentRequestRepository;
import com.example.lawfirm.repository.GeneratedDocumentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final ClientRepository clientRepository;
    private final AttorneyRepository attorneyRepository;
    private final ChamberRepository chamberRepository;
    private final DocumentRequestRepository documentRequestRepository;
    private final GeneratedDocumentRepository generatedDocumentRepository;

    public DashboardService(ClientRepository clientRepository,
                            AttorneyRepository attorneyRepository,
                            ChamberRepository chamberRepository,
                            DocumentRequestRepository documentRequestRepository,
                            GeneratedDocumentRepository generatedDocumentRepository) {
        this.clientRepository = clientRepository;
        this.attorneyRepository = attorneyRepository;
        this.chamberRepository = chamberRepository;
        this.documentRequestRepository = documentRequestRepository;
        this.generatedDocumentRepository = generatedDocumentRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("clients", clientRepository.count());
        summary.put("attorneys", attorneyRepository.count());
        summary.put("freeChambers", (long) chamberRepository.findFreeChambers().size());
        summary.put("requestsToday", documentRequestRepository.countByRequestDate(LocalDate.now()));
        summary.put("draftedRequests", documentRequestRepository.countByStatus(RequestStatus.DRAFTED));
        summary.put("generatedDocuments", generatedDocumentRepository.count());
        return summary;
    }
}
