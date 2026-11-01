package com.example.lawfirm.repository;

import com.example.lawfirm.model.Chamber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChamberRepository extends JpaRepository<Chamber, UUID> {

    @Query("SELECT c FROM Chamber c WHERE NOT EXISTS (SELECT a FROM Attorney a WHERE a.chamber = c) ORDER BY c.chamberNumber ASC")
    List<Chamber> findFreeChambers();

    Optional<Chamber> findByChamberNumber(int chamberNumber);
}
