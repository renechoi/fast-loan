package com.fastcampus.loan.service;

import com.fastcampus.loan.dto.ApplyDTO;
import com.fastcampus.loan.dto.CounselDTO;

public interface ApplicationService {

  ApplyDTO.Response create(ApplyDTO.Request request);

  ApplyDTO.Response get(Long applicationId);
}