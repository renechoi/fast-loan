package com.fastcampus.loan.service;

import com.fastcampus.loan.domain.Apply;
import com.fastcampus.loan.domain.Judgment;
import com.fastcampus.loan.dto.ApplyDTO;
import com.fastcampus.loan.dto.JudgmentDTO;
import com.fastcampus.loan.dto.JudgmentDTO.Request;
import com.fastcampus.loan.dto.JudgmentDTO.Response;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplyRepository;
import com.fastcampus.loan.repository.JudgmentRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JudgmentServiceImpl implements JudgmentService {

  private final JudgmentRepository judgmentRepository;

  private final ApplyRepository applicationRepository;

  private final ModelMapper modelMapper;

  @Override
  public Response create(Request request) {
    Long applicationId = request.getApplicationId();
    if (!isPresentApplication(applicationId)) {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    }

    Judgment judgment = modelMapper.map(request, Judgment.class);

    Judgment saved = judgmentRepository.save(judgment);

    return modelMapper.map(saved, JudgmentDTO.Response.class);
  }

  @Override
  public Response get(Long judgmentId) {
    Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    return modelMapper.map(judgment, JudgmentDTO.Response.class);
  }

  @Override
  public Response getJudgmentOfApplication(Long applicationId) {
    if (!isPresentApplication(applicationId)) {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    }

    Judgment judgment = judgmentRepository.findByApplicationId(applicationId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    return modelMapper.map(judgment, JudgmentDTO.Response.class);
  }

  @Override
  public Response update(Long judgmentId, Request request) {
    Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    judgment.setName(request.getName());
    judgment.setApprovalAmount(request.getApprovalAmount());

    judgmentRepository.save(judgment);

    return modelMapper.map(judgment, JudgmentDTO.Response.class);
  }

  @Override
  public void delete(Long judgmentId) {
    Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    judgment.setIsDeleted(true);

    judgmentRepository.save(judgment);
  }

  @Override
  public ApplyDTO.GrantAmount grant(Long judgmentId) {
    Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    Long applicationId = judgment.getApplicationId();
    Apply apply = applicationRepository.findById(applicationId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    BigDecimal approvalAmount = judgment.getApprovalAmount();
    apply.setApprovalAmount(approvalAmount);

    applicationRepository.save(apply);

    return modelMapper.map(apply, ApplyDTO.GrantAmount.class);
  }

  private boolean isPresentApplication(Long applicationId) {
    return applicationRepository.findById(applicationId).isPresent();
  }
}