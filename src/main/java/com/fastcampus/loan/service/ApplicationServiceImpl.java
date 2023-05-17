package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.ApplyDTO.*;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fastcampus.loan.domain.Apply;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.ApplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

	private final ApplyRepository applyRepository;

	private final ModelMapper modelMapper;

	@Override
	public Response create(Request request) {
		Apply apply = modelMapper.map(request, Apply.class);
		apply.setAppliedAt(LocalDateTime.now());

		Apply applied = applyRepository.save(apply);

		return modelMapper.map(applied, Response.class);
	}

	@Override
	public Response get(Long applicationId) {
		Apply apply = applyRepository.findById(applicationId).orElseThrow(() -> {
			throw new BaseException(ResultType.SYSTEM_ERROR);
		});

		return modelMapper.map(apply, Response.class);
	}

	@Override
	public Response update(Long applicationId, Request request) {
		Apply application = applyRepository.findById(applicationId).orElseThrow(() -> {
			throw new BaseException(ResultType.SYSTEM_ERROR);
		});

		application.setName(request.getName());
		application.setCellPhone(request.getCellPhone());
		application.setEmail(request.getEmail());
		application.setAppliedAmount(request.getHopeAmount());

		applyRepository.save(application);

		return modelMapper.map(application, Response.class);
	}
}