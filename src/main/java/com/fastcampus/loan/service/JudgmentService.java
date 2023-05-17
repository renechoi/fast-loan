package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.JudgmentDTO.*;

import com.fastcampus.loan.dto.ApplyDTO;
import com.fastcampus.loan.dto.JudgmentDTO;

public interface JudgmentService {

  Response create(Request request);

  Response get(Long judgmentId);

  Response getJudgmentOfApplication(Long applicationId);

  Response update(Long judgmentId, Request request);

  ApplyDTO.GrantAmount grant(Long judgmentId);

  void delete(Long judgmentId);
}