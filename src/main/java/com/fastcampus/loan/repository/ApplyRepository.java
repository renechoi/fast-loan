package com.fastcampus.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fastcampus.loan.domain.Apply;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {

}