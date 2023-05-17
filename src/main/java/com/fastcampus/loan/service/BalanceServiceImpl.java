package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.BalanceDTO.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fastcampus.loan.domain.Balance;
import com.fastcampus.loan.dto.BalanceDTO;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.BalanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

  private final BalanceRepository balanceRepository;

  private final ModelMapper modelMapper;

  @Override
  public Response create(Long applicationId, CreateRequest request) {
    Balance balance = modelMapper.map(request, Balance.class);

    // 첫 생성은 entry amount 를 balance
    BigDecimal entryAmount = request.getEntryAmount();
    balance.setApplicationId(applicationId);
    balance.setBalance(entryAmount);

    balanceRepository.findAllByApplicationId(applicationId).ifPresent(b -> {
      balance.setBalanceId(b.getBalanceId());
      balance.setIsDeleted(b.getIsDeleted());
      balance.setCreatedAt(b.getCreatedAt());
      balance.setUpdatedAt(LocalDateTime.now());
    });

    Balance saved = balanceRepository.save(balance);
    return modelMapper.map(saved, Response.class);
  }

  @Override
  public Response get(Long applicationId) {
    Balance balance = balanceRepository.findById(applicationId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    return modelMapper.map(balance, Response.class);
  }

  @Override
  public Response update(Long applicationId, UpdateRequest request) {
    Balance balance = balanceRepository.findAllByApplicationId(applicationId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();
    BigDecimal afterEntryAmount = request.getAfterEntryAmount();
    BigDecimal updatedBalance = balance.getBalance();
    updatedBalance = updatedBalance.subtract(beforeEntryAmount).add(afterEntryAmount);

    balance.setBalance(updatedBalance);

    Balance updated = balanceRepository.save(balance);

    return modelMapper.map(updated, Response.class);
  }

  @Override
  public void delete(Long applicationId) {
    Balance balance = balanceRepository.findAllByApplicationId(applicationId).orElseThrow(() -> {
      throw new BaseException(ResultType.SYSTEM_ERROR);
    });

    balance.setIsDeleted(true);

    balanceRepository.save(balance);
  }
}