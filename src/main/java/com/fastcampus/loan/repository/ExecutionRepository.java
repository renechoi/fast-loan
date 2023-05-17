package com.fastcampus.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastcampus.loan.domain.Execution;

@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {

  Execution findByApplicationId(Long applicationId);
}