package com.phedi.domain.party.validation;

import com.phedi.domain.party.model.item.IdentityDocumentType;

public interface IdentificationValidator {

    boolean supports(String identificationType);

    boolean isValid(String identificationNumber);

    String format(String identificationNumber);

    default boolean supports(IdentityDocumentType identificationType) {
        return identificationType != null && supports(identificationType.name());
    }
}
