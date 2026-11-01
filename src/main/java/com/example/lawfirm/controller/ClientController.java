package com.example.lawfirm.controller;

import com.example.lawfirm.model.Client;
import com.example.lawfirm.service.AttorneyService;
import com.example.lawfirm.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    private final AttorneyService attorneyService;

    public ClientController(ClientService clientService, AttorneyService attorneyService) {
        this.clientService = clientService;
        this.attorneyService = attorneyService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Client client) {
        clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body("Client saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAll() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Client> optional = clientService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The client with that id does not exist");
        }
        return ResponseEntity.ok(optional.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Client client) {
        Optional<Client> optional = clientService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The client with that id does not exist");
        }
        Client current = optional.get();
        current.setFirstName(client.getFirstName());
        current.setLastName(client.getLastName());
        current.setClientCode(client.getClientCode());
        current.setEmail(client.getEmail());
        current.setCompany(client.getCompany());
        Client updated = clientService.save(current);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Optional<Client> optional = clientService.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The client with that id does not exist");
        }
        clientService.deleteById(id);
        return ResponseEntity.ok("Client deleted successfully");
    }

    @GetMapping("/by-company")
    public ResponseEntity<List<Client>> getByCompany(@RequestParam String company) {
        return ResponseEntity.ok(clientService.findByCompany(company));
    }

    @GetMapping("/of-attorney/{attorneyId}")
    public ResponseEntity<?> getClientsOfAttorney(@PathVariable UUID attorneyId) {
        if (!attorneyService.existsById(attorneyId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The attorney with that id does not exist");
        }
        return ResponseEntity.ok(clientService.getClientsOfAttorney(attorneyId));
    }
}
