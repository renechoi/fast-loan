package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.ApplyDTO.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fastcampus.loan.domain.AcceptTerms;
import com.fastcampus.loan.domain.Apply;
import com.fastcampus.loan.domain.Terms;
import com.fastcampus.loan.dto.ApplyDTO;
import com.fastcampus.loan.exception.BaseException;
import com.fastcampus.loan.exception.ResultType;
import com.fastcampus.loan.repository.AcceptTermsRepository;
import com.fastcampus.loan.repository.ApplyRepository;
import com.fastcampus.loan.repository.TermsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

	private final ApplyRepository applyRepository;
	private final TermsRepository termsRepository;

	private final AcceptTermsRepository acceptTermsRepository;

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

	@Override
	public void delete(Long applicationId) {
		Apply application = applyRepository.findById(applicationId).orElseThrow(() -> {
			throw new BaseException(ResultType.SYSTEM_ERROR);
		});

		application.setIsDeleted(true);

		applyRepository.save(application);
	}

	@Override
	public Boolean acceptTerms(Long applicationId, ApplyDTO.AcceptTerms dto) {
		applyRepository.findById(applicationId).orElseThrow(() -> {
			throw new BaseException(ResultType.SYSTEM_ERROR);
		});

		List<Terms> termsList = termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"));
		if (termsList.isEmpty()) {
			throw new BaseException(ResultType.SYSTEM_ERROR);
		}

		List<Long> acceptTermsIds = dto.getAcceptTermsIds();
		if (termsList.size() != acceptTermsIds.size()) {
			throw new BaseException(ResultType.SYSTEM_ERROR);
		}

		List<Long> termsIds = termsList.stream().map(Terms::getTermsId).collect(Collectors.toList());
		Collections.sort(acceptTermsIds);

		if (!termsIds.containsAll(acceptTermsIds)) {
			throw new BaseException(ResultType.SYSTEM_ERROR);
		}

		for (Long termsId : acceptTermsIds) {
			AcceptTerms accepted = AcceptTerms.builder()
				.termsId(termsId)
				.applicationId(applicationId)
				.build();

			acceptTermsRepository.save(accepted);
		}

		return true;
	}
}