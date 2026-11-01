package com.example.lawfirm.service;

import com.example.lawfirm.model.Client;
import com.example.lawfirm.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(UUID id) {
        return clientRepository.findById(id);
    }

    public boolean existsById(UUID id) {
        return clientRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        clientRepository.deleteById(id);
    }

    public List<Client> findByCompany(String company) {
        return clientRepository.findByCompanyIgnoreCaseOrderByLastNameAsc(company);
    }

    public List<Client> getClientsOfAttorney(UUID attorneyId) {
        return clientRepository.findClientsByAttorneyId(attorneyId);
    }
}
