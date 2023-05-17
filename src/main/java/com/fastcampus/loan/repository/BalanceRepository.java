package com.fastcampus.loan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastcampus.loan.domain.Balance;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

  Optional<Balance> findAllByApplicationId(Long applicationId);

  Optional<Balance> findByApplicationId(Long applicationId);
}