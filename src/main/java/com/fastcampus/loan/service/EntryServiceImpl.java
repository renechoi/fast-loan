package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.ExecutionDTO.*;

import java.math.BigDecimal;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fastcampus.loan.domain.Apply;
import com.fastcampus.loan.domain.Execution;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplyRepository;
import com.fastcampus.loan.repository.ExecutionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

  private final ExecutionRepository executionRepository;

  private final ApplyRepository applicationRepository;

  private final BalanceService balanceService;

  private final ModelMapper modelMapper;

  @Transactional
  @Override
  public Response create(Long applicationId, Request request) {
    if (!isContractApplication(applicationId)) {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    }

    Execution execution = modelMapper.map(request, Execution.class);
    execution.setApplicationId(applicationId);

    executionRepository.save(execution);

    balanceService.create(applicationId,
        BalanceDTO.CreateRequest.builder()
            .entryAmount(request.getEntryAmount())
            .build()
    );

    return modelMapper.map(execution, Response.class);
  }

  @Override
  public Response get(Long applicationId) {
    Optional<Execution> execution = executionRepository.findByApplicationId(applicationId);

    return modelMapper.map(execution, Response.class);
  }

  @Transactional
  @Override
  public UpdateResponse update(Long entryId, Request request) {
    Execution execution = executionRepository.findById(entryId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    BigDecimal beforeEntryAmount = execution.getEntryAmount();
    execution.setEntryAmount(request.getEntryAmount());

    executionRepository.save(execution);

    Long applicationId = execution.getApplicationId();
    balanceService.update(applicationId,
        BalanceDTO.UpdateRequest.builder()
            .beforeEntryAmount(beforeEntryAmount)
            .afterEntryAmount(request.getEntryAmount())
            .build()
    );

    return UpdateResponse.builder()
        .applicationId(execution.getApplicationId())
        .beforeEntryAmount(beforeEntryAmount)
        .afterEntryAmount(execution.getEntryAmount())
        .build();
  }

  @Transactional
  @Override
  public void delete(Long entryId) {
    Execution execution = executionRepository.findById(entryId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    execution.setIsDeleted(true);

    executionRepository.save(execution);

    BigDecimal beforeEntryAmount = execution.getEntryAmount();

    Long applicationId = execution.getApplicationId();
    balanceService.update(applicationId,
        BalanceDTO.UpdateRequest.builder()
            .beforeEntryAmount(beforeEntryAmount)
            .afterEntryAmount(BigDecimal.ZERO)
            .build()
    );
  }

  private boolean isContractApplication(Long applicationId) {
    Optional<Apply> existed = applicationRepository.findById(applicationId);
    if (existed.isEmpty()) {
      return false;
    }

    return existed.get().getContractAt() != null;
  }
}