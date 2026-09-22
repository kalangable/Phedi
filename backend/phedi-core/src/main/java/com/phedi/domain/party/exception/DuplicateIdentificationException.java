package com.phedi.domain.party.exception;

public class DuplicateIdentificationException extends RuntimeException {
    
    public DuplicateIdentificationException(String message) {
        super(message);
    }
    
    public DuplicateIdentificationException(String identificationType, String identificationNumber) {
        super(String.format("Duplicate %s already exists: %s", identificationType, identificationNumber));
    }
}
