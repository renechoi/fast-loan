package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.BalanceDTO.*;

import com.fastcampus.loan.dto.BalanceDTO;

public interface BalanceService {

  Response create(Long applicationId, CreateRequest request);

  Response get(Long applicationId);

  Response update(Long applicationId, UpdateRequest request);

  void delete(Long applicationId);
}