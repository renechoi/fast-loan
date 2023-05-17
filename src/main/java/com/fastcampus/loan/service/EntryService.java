package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.ExecutionDTO.*;

public interface EntryService {

  Response create(Long applicationId, Request request);

  Response get(Long applicationId);

  UpdateResponse update(Long entryId, Request request);

  void delete(Long entryId);
}