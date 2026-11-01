package com.example.lawfirm.repository;

import com.example.lawfirm.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    List<Client> findByCompanyIgnoreCaseOrderByLastNameAsc(String company);

    @Query("SELECT DISTINCT dr.client FROM DocumentRequest dr WHERE dr.attorney.id = :attorneyId")
    List<Client> findClientsByAttorneyId(@Param("attorneyId") UUID attorneyId);
}
