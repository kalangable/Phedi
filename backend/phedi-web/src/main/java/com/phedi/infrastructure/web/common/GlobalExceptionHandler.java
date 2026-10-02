package com.phedi.infrastructure.web.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.phedi.domain.party.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail notFound(HttpServletRequest request, ResourceNotFoundException ex) {
        return problem(request, HttpStatus.NOT_FOUND, ex.getMessage());
    }

    private ProblemDetail problem(HttpServletRequest request, HttpStatus status, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        String requestId = request.getHeader(RequestIdFilter.HEADER);
        if (requestId != null && !requestId.isBlank()) {
            problem.setProperty(RequestIdFilter.MDC_KEY, requestId);
        }
        return problem;
    }
}