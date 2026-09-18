package com.phedi.domain.party.exception;

public class InvalidIdentificationException extends RuntimeException {
    
    public InvalidIdentificationException(String message) {
        super(message);
    }

/*     public InvalidIdentificationException(String identificationType) {
        super(String.format("Invalid %s", identificationType));
    }*/

    public InvalidIdentificationException(String identificationType, String identificationNumber) {
        super(String.format("Invalid %s: %s", identificationType, identificationNumber));
    }
}

