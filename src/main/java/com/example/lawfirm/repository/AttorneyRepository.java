package com.example.lawfirm.repository;

import com.example.lawfirm.model.Attorney;
import com.example.lawfirm.model.Chamber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttorneyRepository extends JpaRepository<Attorney, UUID> {

    @Query("SELECT DISTINCT a FROM Attorney a JOIN a.practiceAreas p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Attorney> findByPracticeAreaName(@Param("name") String name);

    boolean existsByChamber(Chamber chamber);
}
