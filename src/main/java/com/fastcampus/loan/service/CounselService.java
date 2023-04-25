package com.fastcampus.loan.service;

import static com.fastcampus.loan.dto.CounselDTO.*;

public interface CounselService {
	Response create(Request request);

	Response get(Long counselId);

}
