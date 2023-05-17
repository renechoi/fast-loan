package com.fastcampus.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastcampus.loan.domain.Terms;

@Repository
public interface TermsRepository extends JpaRepository<Terms, Long> {

}