package com.example.lawfirm.repository;

import com.example.lawfirm.model.DocumentRequest;
import com.example.lawfirm.model.GeneratedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GeneratedDocumentRepository extends JpaRepository<GeneratedDocument, UUID> {

    @Query("SELECT gd FROM GeneratedDocument gd WHERE gd.documentRequest.client.clientCode = :clientCode ORDER BY gd.generatedDate DESC")
    List<GeneratedDocument> findByClientCode(@Param("clientCode") String clientCode);

    boolean existsByDocumentRequest(DocumentRequest documentRequest);

    boolean existsByDocumentRequestId(UUID documentRequestId);
}
