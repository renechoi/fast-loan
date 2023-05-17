package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.ApplyDTO.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.fastcampus.loan.domain.Apply;
import com.fastcampus.loan.dto.ApplyDTO;
import com.fastcampus.loan.repository.ApplyRepository;

@ExtendWith(MockitoExtension.class)
public class ApplyServiceTest {

  @InjectMocks
  ApplicationServiceImpl applicationService;

  @Mock
  private ApplyRepository applicationRepository;

  @Spy
  private ModelMapper modelMapper;

  @Test
  void Should_ReturnResponseOfNewApplyEntity_When_RequestApply() {
    Apply entity = Apply.builder()
        .name("Member Kim")
        .cellPhone("010-1111-2222")
        .email("mail@abc.de")
        .appliedAmount(BigDecimal.valueOf(50000000))
        .build();

    Request request = Request.builder()
        .name("Member Kim")
        .cellPhone("010-1111-2222")
        .email("mail@abc.de")
        .hopeAmount(BigDecimal.valueOf(50000000))
        .build();

    when(applicationRepository.save(ArgumentMatchers.any(Apply.class))).thenReturn(entity);

    Response actual = applicationService.create(request);

    assertThat(actual.getName()).isSameAs(entity.getName());
  }

  @Test
  void Should_ReturnResponseOfExistApplicationEntity_When_RequestExistApplicationId() {
    Long findId = 1L;

    Apply entity = Apply.builder()
        .applyId(1L)
        .build();

    when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

    Response actual = applicationService.get(1L);

    assertThat(actual.getApplyId()).isSameAs(findId);
  }

  @Test
  void Should_ReturnUpdatedResponseOfExistApplicationEntity_When_RequestUpdateExistApplicationInfo() {
    Long findId = 1L;

    Apply entity = Apply.builder()
        .applyId(1L)
        .name("Member Kim")
        .build();

    Request request = Request.builder()
        .name("Member Lee")
        .build();

    when(applicationRepository.save(ArgumentMatchers.any(Apply.class))).thenReturn(entity);
    when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

    Response actual = applicationService.update(findId, request);

    assertThat(actual.getApplyId()).isSameAs(findId);
    assertThat(actual.getName()).isSameAs(request.getName());
  }
}