package com.phedi.infrastructure.web.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.phedi.domain.party.exception.ResourceNotFoundException;
import com.phedi.infrastructure.web.problem.ProblemDetailFactory;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice 
@RequiredArgsConstructor 
public class DomainExceptionHandler {

    private final ProblemDetailFactory problem;

    @ExceptionHandler (ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(HttpServletRequest request, ResourceNotFoundException ex) {
        return problem.create(request, HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
