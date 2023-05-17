package com.fastcampus.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastcampus.loan.domain.AcceptTerms;

@Repository
public interface AcceptTermsRepository extends JpaRepository<AcceptTerms, Long> {

}