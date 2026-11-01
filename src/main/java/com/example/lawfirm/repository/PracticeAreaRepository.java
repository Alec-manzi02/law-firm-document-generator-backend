package com.example.lawfirm.repository;

import com.example.lawfirm.model.PracticeArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PracticeAreaRepository extends JpaRepository<PracticeArea, UUID> {

    @Query("SELECT p FROM PracticeArea p WHERE p.attorneys IS EMPTY")
    List<PracticeArea> findUnusedPracticeAreas();
}
