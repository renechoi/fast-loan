package com.fastcampus.loan.service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fastcampus.loan.domain.Apply;
import com.fastcampus.loan.dto.ApplyDTO;
import com.fastcampus.loan.repository.ApplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

  private final ApplyRepository applyRepository;

  private final ModelMapper modelMapper;

  @Override
  public ApplyDTO.Response create(ApplyDTO.Request request) {
    Apply apply = modelMapper.map(request, Apply.class);
    apply.setAppliedAt(LocalDateTime.now());

    Apply applied = applyRepository.save(apply);

    return modelMapper.map(applied, ApplyDTO.Response.class);
  }
}