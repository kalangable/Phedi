package com.phedi.infrastructure.web.common;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.phedi.infrastructure.web.problem.ProblemDetailFactory;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ProblemDetailFactory problem;

    private static final String DEFAULT_VALIDATION_MESSAGE = "invalid value";
    private static final String VALIDATION_FAILED = "Validation failed";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpServletRequest servletRequest = getHttpServletRequest(request);

        ProblemDetail problemDetail = problem.create(servletRequest, HttpStatus.BAD_REQUEST, buildValidationDetail(ex));

        problemDetail.setProperty("errors", buildValidationErrors(ex));

        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(problemDetail);
    }

    private Map<String, String> buildValidationErrors(MethodArgumentNotValidException ex) {

        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        this::getErrorMessage,
                        (first, second) -> first + ", " + second,
                        LinkedHashMap::new));
    }

    private String buildValidationDetail(MethodArgumentNotValidException ex) {

        String fields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + getErrorMessage(error))
                .collect(Collectors.joining(", "));

        return fields.isBlank()
                ? VALIDATION_FAILED
                : VALIDATION_FAILED + ": " + fields;
    }

    private String getErrorMessage(FieldError error) {
        return error.getDefaultMessage() != null
                ? error.getDefaultMessage()
                : DEFAULT_VALIDATION_MESSAGE;
    }

    private HttpServletRequest getHttpServletRequest(WebRequest request) {
        return ((ServletWebRequest) request).getRequest();
    }

}