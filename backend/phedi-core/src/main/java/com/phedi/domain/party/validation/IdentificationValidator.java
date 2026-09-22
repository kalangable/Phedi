package com.phedi.domain.party.validation;

public interface IdentificationValidator {

    boolean supports(String identificationType);
    
    boolean isValid(String identificationNumber);
    
    String format(String identificationNumber);

}
