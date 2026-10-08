package com.phedi.infrastructure.web.problem;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import com.phedi.infrastructure.web.common.RequestIdFilter;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class ProblemDetailFactory {

    public ProblemDetail create(HttpServletRequest request, HttpStatus status, String detail) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        
        String requestId = request.getHeader(RequestIdFilter.HEADER);
        if (requestId != null && !requestId.isBlank()) {
            problem.setProperty(RequestIdFilter.MDC_KEY, requestId);
        }

        return problem;
    }
}
