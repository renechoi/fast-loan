package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.RepaymentDTO.*;

import java.util.List;

import com.fastcampus.loan.dto.RepaymentDTO;

public interface RepaymentService {

  Response create(Long applicationId, Request request);

  List<ListResponse> get(Long applicationId);

  UpdateResponse update(Long repaymentId, Request request);

  void delete(Long repaymentId);
}