package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.ApplyDTO.*;

import com.fastcampus.loan.dto.ApplyDTO;
import com.fastcampus.loan.dto.CounselDTO;

public interface ApplicationService {

  Response create(Request request);
  Response get(Long applicationId);
  Response update(Long applicationId, Request request);
}