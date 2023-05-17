package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Apply;
import com.fastcampus.loan.domain.Execution;
import com.fastcampus.loan.domain.Repayment;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.dto.RepaymentDTO.ListResponse;
import com.fastcampus.loan.dto.RepaymentDTO.Request;
import com.fastcampus.loan.dto.RepaymentDTO.Response;
import com.fastcampus.loan.dto.RepaymentDTO.UpdateResponse;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplyRepository;
import com.fastcampus.loan.repository.ExecutionRepository;
import com.fastcampus.loan.repository.RepaymentRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService {

  private final RepaymentRepository repaymentRepository;

  private final ApplyRepository applicationRepository;

  private final ExecutionRepository entryRepository;

  private final BalanceService balanceService;

  private final ModelMapper modelMapper;

  @Override
  public Response create(Long applicationId, Request request) {
    if (!isRepayableApplication(applicationId)) {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    }

    Repayment repayment = modelMapper.map(request, Repayment.class);
    repayment.setApplicationId(applicationId);

    repaymentRepository.save(repayment);

    BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
        BalanceDTO.RepaymentRequest.builder()
            .repaymentAmount(request.getRepaymentAmount())
            .type(BalanceDTO.RepaymentRequest.RepaymentType.REMOVE)
            .build());

    Response response = modelMapper.map(repayment, Response.class);
    response.setBalance(updatedBalance.getBalance());

    return response;
  }

  @Override
  public List<ListResponse> get(Long applicationId) {
    List<Repayment> repayments = repaymentRepository.findAllByApplicationId(applicationId);

    return repayments.stream().map(r -> modelMapper.map(r, ListResponse.class)).collect(Collectors.toList());
  }

  @Override
  public UpdateResponse update(Long repaymentId, Request request) {
    Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    Long applicationId = repayment.getApplicationId();
    BigDecimal beforeRepaymentAmount = repayment.getRepaymentAmount();

    balanceService.repaymentUpdate(applicationId,
        BalanceDTO.RepaymentRequest.builder()
            .repaymentAmount(beforeRepaymentAmount)
            .type(BalanceDTO.RepaymentRequest.RepaymentType.ADD)
            .build()
    );

    repayment.setRepaymentAmount(request.getRepaymentAmount());
    repaymentRepository.save(repayment);

    BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
        BalanceDTO.RepaymentRequest.builder()
            .repaymentAmount(request.getRepaymentAmount())
            .type(BalanceDTO.RepaymentRequest.RepaymentType.REMOVE)
            .build()
    );

    return UpdateResponse.builder()
        .applicationId(applicationId)
        .beforeRepaymentAmount(beforeRepaymentAmount)
        .afterRepaymentAmount(request.getRepaymentAmount())
        .balance(updatedBalance.getBalance())
        .createdAt(repayment.getCreatedAt())
        .updatedAt(repayment.getUpdatedAt())
        .build();
  }

  @Override
  public void delete(Long repaymentId) {
    Repayment repayment = repaymentRepository.findById(repaymentId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    Long applicationId = repayment.getApplicationId();
    BigDecimal removeRepaymentAmount = repayment.getRepaymentAmount();

    balanceService.repaymentUpdate(applicationId
        , BalanceDTO.RepaymentRequest.builder()
            .repaymentAmount(removeRepaymentAmount)
            .type(BalanceDTO.RepaymentRequest.RepaymentType.ADD)
            .build());

    repayment.setIsDeleted(true);
    repaymentRepository.save(repayment);
  }

  private boolean isRepayableApplication(Long applicationId) {
    Optional<Apply> existedApplication = applicationRepository.findById(applicationId);
    if (existedApplication.isEmpty()) {
      return false;
    }

    if (existedApplication.get().getContractAt() == null) {
      return false;
    }

    Optional<Execution> existedEntry = entryRepository.findByApplicationId(applicationId);
    return existedEntry.isPresent();
  }
}