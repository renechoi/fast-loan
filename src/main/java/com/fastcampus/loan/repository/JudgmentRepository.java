package com.fastcampus.loan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastcampus.loan.domain.Judgment;

@Repository
public interface JudgmentRepository extends JpaRepository<Judgment, Long> {

  Optional<Judgment> findByApplicationId(Long applicationId);
}