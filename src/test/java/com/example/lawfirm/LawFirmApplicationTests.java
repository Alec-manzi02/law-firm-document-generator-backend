package com.example.lawfirm;

import com.example.lawfirm.model.*;
import com.example.lawfirm.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LawFirmApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GeneratedDocumentRepository generatedDocumentRepository;

    @Autowired
    private DocumentRequestRepository documentRequestRepository;

    @Autowired
    private AttorneyRepository attorneyRepository;

    @Autowired
    private PracticeAreaRepository practiceAreaRepository;

    @Autowired
    private ChamberRepository chamberRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setup() {
        generatedDocumentRepository.deleteAll();
        documentRequestRepository.deleteAll();
        attorneyRepository.deleteAll();
        practiceAreaRepository.deleteAll();
        chamberRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    public void testPartACrudPracticeArea() throws Exception {
        PracticeArea pa = new PracticeArea();
        pa.setName("Corporate Law");

        // Save
        mockMvc.perform(post("/api/practice-areas/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pa)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Practice area saved successfully"));

        PracticeArea saved = practiceAreaRepository.findAll().get(0);

        // Get All
        mockMvc.perform(get("/api/practice-areas/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Corporate Law"));

        // Get By Id
        mockMvc.perform(get("/api/practice-areas/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Corporate Law"));

        // Update
        saved.setName("Commercial Law");
        mockMvc.perform(put("/api/practice-areas/update/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Commercial Law"));

        // Delete
        mockMvc.perform(delete("/api/practice-areas/delete/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Practice area deleted successfully"));

        // Not Found
        mockMvc.perform(get("/api/practice-areas/" + saved.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The practice area with that id does not exist"));
    }

    @Test
    public void testPartACrudChamberAndClient() throws Exception {
        // Chamber
        Chamber chamber = new Chamber(null, 101, "East Wing 1");
        mockMvc.perform(post("/api/chambers/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chamber)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Chamber saved successfully"));

        Chamber savedChamber = chamberRepository.findAll().get(0);

        // Client
        Client client = new Client(null, "Alice", "Mugabe", "C-001", "alice@inyange.rw", "Inyange");
        mockMvc.perform(post("/api/clients/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Client saved successfully"));

        Client savedClient = clientRepository.findAll().get(0);

        mockMvc.perform(get("/api/clients/" + savedClient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientCode").value("C-001"));
    }

    @Test
    public void testPartBDerivedQueries() throws Exception {
        Client c1 = clientRepository.save(new Client(null, "John", "Kamanzi", "C-002", "john@inyange.rw", "inyange"));
        Client c2 = clientRepository.save(new Client(null, "Beata", "Gasana", "C-003", "beata@inyange.rw", "Inyange"));
        Client c3 = clientRepository.save(new Client(null, "Eric", "Ruzindana", "C-004", "eric@mtn.rw", "MTN"));

        // B1: GET /api/clients/by-company?company=inyange
        mockMvc.perform(get("/api/clients/by-company?company=inyange"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].lastName").value("Gasana"))
                .andExpect(jsonPath("$[1].lastName").value("Kamanzi"));

        // B2 & B3 Document Requests
        DocumentRequest dr1 = new DocumentRequest(null, LocalDate.now().plusDays(2), RequestStatus.DRAFTED, "NDA", "Rwanda", c1, null);
        DocumentRequest dr2 = new DocumentRequest(null, LocalDate.now().plusDays(5), RequestStatus.REVIEWED, "MOU", "Rwanda", c2, null);
        DocumentRequest dr3 = new DocumentRequest(null, LocalDate.now().plusDays(1), RequestStatus.DRAFTED, "Contract", "Rwanda", c3, null);

        documentRequestRepository.save(dr1);
        documentRequestRepository.save(dr2);
        documentRequestRepository.save(dr3);

        // B2: GET /api/document-requests/by-status?status=DRAFTED
        mockMvc.perform(get("/api/document-requests/by-status?status=DRAFTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].templateName").value("Contract"))
                .andExpect(jsonPath("$[1].templateName").value("NDA"));

        // B3: GET /api/document-requests/between?start=...&end=...
        String start = LocalDate.now().toString();
        String end = LocalDate.now().plusDays(3).toString();
        mockMvc.perform(get("/api/document-requests/between?start=" + start + "&end=" + end))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testPartCJPQLWithJoins() throws Exception {
        PracticeArea paCorp = practiceAreaRepository.save(new PracticeArea(null, "Corporate Law"));
        PracticeArea paTax = practiceAreaRepository.save(new PracticeArea(null, "Tax Law"));
        PracticeArea paUnused = practiceAreaRepository.save(new PracticeArea(null, "Family Law"));

        Chamber ch1 = chamberRepository.save(new Chamber(null, 201, "Chamber 201"));
        Chamber chFree = chamberRepository.save(new Chamber(null, 202, "Chamber 202"));

        Attorney att = new Attorney(null, "Paul", "Kagabo", "BAR-100", ch1, new ArrayList<>());
        att.getPracticeAreas().add(paCorp);
        att.getPracticeAreas().add(paTax);
        att = attorneyRepository.save(att);

        Client c1 = clientRepository.save(new Client(null, "David", "Muhire", "C-005", "david@corp.rw", "Corp"));
        documentRequestRepository.save(new DocumentRequest(null, LocalDate.now().plusDays(3), RequestStatus.DRAFTED, "Merger", "Rwanda", c1, att));

        // C1: by-practice-area
        mockMvc.perform(get("/api/attorneys/by-practice-area?name=corp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Paul"));

        // C2: free chambers
        mockMvc.perform(get("/api/chambers/free"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].chamberNumber").value(202));

        // C3: unused practice areas
        mockMvc.perform(get("/api/practice-areas/unused"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Family Law"));

        // C4: clients of attorney
        mockMvc.perform(get("/api/clients/of-attorney/" + att.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].clientCode").value("C-005"));
    }

    @Test
    public void testPartDBusinessRules() throws Exception {
        Attorney att = attorneyRepository.save(new Attorney(null, "Sarah", "Uwase", "BAR-200", null, new ArrayList<>()));
        Client client = clientRepository.save(new Client(null, "Jane", "Doe", "C-006", "jane@test.rw", "TestOrg"));

        // D1 Safe Saving: past date rejected
        DocumentRequest pastReq = new DocumentRequest(null, LocalDate.now().minusDays(1), null, "NDA", "Rwanda", client, att);
        mockMvc.perform(post("/api/document-requests/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pastReq)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Document request date cannot be in the past"));

        // D1 Safe Saving: first save succeeds as DRAFTED
        LocalDate targetDate = LocalDate.now().plusDays(10);
        DocumentRequest validReq = new DocumentRequest(null, targetDate, RequestStatus.EXECUTED, "NDA", "Rwanda", client, att);
        mockMvc.perform(post("/api/document-requests/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReq)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Document request saved successfully"));

        DocumentRequest savedReq = documentRequestRepository.findAll().get(0);
        assertEquals(RequestStatus.DRAFTED, savedReq.getStatus());

        // D1 Safe Saving: attorney conflict on same date
        DocumentRequest conflictReq = new DocumentRequest(null, targetDate, null, "MOU", "Rwanda", client, att);
        mockMvc.perform(post("/api/document-requests/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conflictReq)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Attorney already has a document request on that date"));

        // D2 Status Flow
        // DRAFTED -> EXECUTED (invalid)
        mockMvc.perform(patch("/api/document-requests/" + savedReq.getId() + "/status?status=EXECUTED"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot change status from DRAFTED to EXECUTED"));

        // DRAFTED -> REVIEWED (valid)
        mockMvc.perform(patch("/api/document-requests/" + savedReq.getId() + "/status?status=REVIEWED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Document request status updated successfully"));

        // REVIEWED -> EXECUTED (valid)
        mockMvc.perform(patch("/api/document-requests/" + savedReq.getId() + "/status?status=EXECUTED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Document request status updated successfully"));

        // EXECUTED -> VOIDED (invalid)
        mockMvc.perform(patch("/api/document-requests/" + savedReq.getId() + "/status?status=VOIDED"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot change status from EXECUTED to VOIDED"));

        // D3 Add Practice Area
        PracticeArea pa = practiceAreaRepository.save(new PracticeArea(null, "Maritime Law"));
        mockMvc.perform(put("/api/attorneys/" + att.getId() + "/practice-areas/" + pa.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Practice area added to the attorney"));

        // D3 conflict if already added
        mockMvc.perform(put("/api/attorneys/" + att.getId() + "/practice-areas/" + pa.getId()))
                .andExpect(status().isConflict())
                .andExpect(content().string("Attorney already has this practice area"));

        // D4 Move Chamber
        Chamber ch = chamberRepository.save(new Chamber(null, 305, "Chamber 305"));
        mockMvc.perform(patch("/api/attorneys/" + att.getId() + "/chamber?chamberNumber=305"))
                .andExpect(status().isOk())
                .andExpect(content().string("Chamber changed successfully"));

        // Chamber taken
        Attorney att2 = attorneyRepository.save(new Attorney(null, "Kevin", "Nkusi", "BAR-201", null, new ArrayList<>()));
        mockMvc.perform(patch("/api/attorneys/" + att2.getId() + "/chamber?chamberNumber=305"))
                .andExpect(status().isConflict())
                .andExpect(content().string("That chamber is taken by another attorney"));
    }

    @Test
    public void testPartEGeneratedDocumentsAndProtection() throws Exception {
        Client client = clientRepository.save(new Client(null, "Nadine", "Ingabire", "C-007", "nadine@domain.rw", "Domain"));
        DocumentRequest reqDrafted = documentRequestRepository.save(
                new DocumentRequest(null, LocalDate.now().plusDays(4), RequestStatus.DRAFTED, "Lease", "Rwanda", client, null));
        DocumentRequest reqExecuted = documentRequestRepository.save(
                new DocumentRequest(null, LocalDate.now().plusDays(4), RequestStatus.EXECUTED, "Lease", "Rwanda", client, null));

        GeneratedDocument genDoc = new GeneratedDocument(null, "PDF", "Final lease agreement", 12, null, null);

        // E1: only for EXECUTED requests
        mockMvc.perform(post("/api/generated-documents/save?documentRequestId=" + reqDrafted.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genDoc)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Generated documents are only for EXECUTED document requests"));

        // E1: Success for EXECUTED
        mockMvc.perform(post("/api/generated-documents/save?documentRequestId=" + reqExecuted.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genDoc)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Generated document saved successfully"));

        GeneratedDocument savedGenDoc = generatedDocumentRepository.findAll().get(0);
        assertEquals(LocalDate.now(), savedGenDoc.getGeneratedDate());

        // E2: GET /api/generated-documents/client/{clientCode}
        mockMvc.perform(get("/api/generated-documents/client/C-007"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].documentType").value("PDF"));

        // E3: Protect DocumentRequest delete
        mockMvc.perform(delete("/api/document-requests/delete/" + reqExecuted.getId()))
                .andExpect(status().isConflict())
                .andExpect(content().string("That document request has generated documents, delete them first"));

        // E3: Delete generated document
        mockMvc.perform(delete("/api/generated-documents/delete/" + savedGenDoc.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Generated document deleted successfully"));

        // Now document request can be deleted
        mockMvc.perform(delete("/api/document-requests/delete/" + reqExecuted.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Document request deleted successfully"));
    }

    @Test
    public void testPartFAndBonus() throws Exception {
        Attorney att = attorneyRepository.save(new Attorney(null, "Jean", "Habimana", "BAR-300", null, new ArrayList<>()));
        LocalDate date = LocalDate.now().plusDays(7);

        DocumentRequest dr1 = documentRequestRepository.save(new DocumentRequest(null, date, RequestStatus.DRAFTED, "Req1", "Rwanda", null, att));
        DocumentRequest dr2 = documentRequestRepository.save(new DocumentRequest(null, date, RequestStatus.REVIEWED, "Req2", "Rwanda", null, att));
        DocumentRequest dr3 = documentRequestRepository.save(new DocumentRequest(null, date, RequestStatus.EXECUTED, "Req3", "Rwanda", null, att));

        // F1: stats by status
        mockMvc.perform(get("/api/document-requests/stats/by-status"))
                .andExpect(status().isOk());

        // F2: cancel-day (cancels DRAFTED and REVIEWED, leaves EXECUTED)
        mockMvc.perform(patch("/api/document-requests/cancel-day?attorneyId=" + att.getId() + "&date=" + date))
                .andExpect(status().isOk())
                .andExpect(content().string("2 document requests cancelled"));

        DocumentRequest updatedDr1 = documentRequestRepository.findById(dr1.getId()).get();
        DocumentRequest updatedDr2 = documentRequestRepository.findById(dr2.getId()).get();
        DocumentRequest updatedDr3 = documentRequestRepository.findById(dr3.getId()).get();

        assertEquals(RequestStatus.VOIDED, updatedDr1.getStatus());
        assertEquals(RequestStatus.VOIDED, updatedDr2.getStatus());
        assertEquals(RequestStatus.EXECUTED, updatedDr3.getStatus());

        // Bonus 1: Dashboard
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attorneys").value(1));

        // Bonus 2: Pagination
        mockMvc.perform(get("/api/document-requests/page?page=0&size=2&sort=requestDate,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(3));
    }
}
