package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.ApplyDTO.*;

import com.fastcampus.loan.domain.AcceptTerms;
import com.fastcampus.loan.dto.ApplyDTO;
import com.fastcampus.loan.dto.CounselDTO;

public interface ApplicationService {

  Response create(Request request);
  Response get(Long applicationId);
  Response update(Long applicationId, Request request);
  void delete(Long applicationId);

  Boolean acceptTerms(Long applicationId, ApplyDTO.AcceptTerms request);

  Response contract(Long applicationId);
}