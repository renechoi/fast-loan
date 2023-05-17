package com.fastcampus.loan.controller;

import static com.fastcampus.loan.dto.TermsDTO.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.dto.TermsDTO;
import com.fastcampus.loan.service.TermsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/terms")
public class TermsController extends AbstractController {

  private final TermsService termsService;

  @PostMapping
  public ResponseDTO<Response> create(@RequestBody Request request) {
    return ok(termsService.create(request));
  }

  @GetMapping()
  public ResponseDTO<List<Response>> getAll() {
    return ok(termsService.getAll());
  }
}