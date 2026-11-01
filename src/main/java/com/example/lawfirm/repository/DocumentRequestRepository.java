package com.example.lawfirm.repository;

import com.example.lawfirm.model.DocumentRequest;
import com.example.lawfirm.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRequestRepository extends JpaRepository<DocumentRequest, UUID> {

    List<DocumentRequest> findByStatusOrderByRequestDateAsc(RequestStatus status);

    List<DocumentRequest> findByRequestDateBetweenOrderByRequestDateAsc(LocalDate start, LocalDate end);

    boolean existsByAttorneyIdAndRequestDateAndStatusNot(UUID attorneyId, LocalDate requestDate, RequestStatus status);

    @Query("SELECT dr.status, COUNT(dr) FROM DocumentRequest dr GROUP BY dr.status")
    List<Object[]> getStatsByStatus();

    @Modifying
    @Transactional
    @Query("UPDATE DocumentRequest dr SET dr.status = com.example.lawfirm.model.RequestStatus.VOIDED WHERE dr.attorney.id = :attorneyId AND dr.requestDate = :date AND dr.status <> com.example.lawfirm.model.RequestStatus.EXECUTED")
    int cancelDayForAttorney(@Param("attorneyId") UUID attorneyId, @Param("date") LocalDate date);

    long countByRequestDate(LocalDate requestDate);

    long countByStatus(RequestStatus status);
}
