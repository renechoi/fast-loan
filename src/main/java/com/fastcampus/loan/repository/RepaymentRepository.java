package com.fastcampus.loan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastcampus.loan.domain.Repayment;

@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, Long> {

  List<Repayment> findAllByApplicationId(Long applicationId);
}