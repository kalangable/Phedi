package com.phedi.domain.party.validation;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import com.phedi.domain.party.model.item.IdentityDocumentType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdentificationValidationService {

    private final List<IdentificationValidator> validators;
    private final DefaultIdentificationValidator defaultValidator;

    public boolean validate(String identificationType, String identificationNumber) {
        return findValidator(identificationType).isValid(identificationNumber);
    }

    public boolean validate(IdentityDocumentType identificationType, String identificationNumber) {
        return findValidator(identificationType).isValid(identificationNumber);
    }

    public String format(String identificationType, String identificationNumber) {
        return findValidator(identificationType).format(identificationNumber);
    }

    public String format(IdentityDocumentType identificationType, String identificationNumber) {
        return findValidator(identificationType).format(identificationNumber);
    }

    private IdentificationValidator findValidator(String identificationType) {
        return firstSupporting(validator -> validator.supports(identificationType));
    }

    private IdentificationValidator findValidator(IdentityDocumentType identificationType) {
        return firstSupporting(validator -> validator.supports(identificationType));
    }

    private IdentificationValidator firstSupporting(Predicate<IdentificationValidator> support) {
        return validators.stream()
                .filter(validator -> !(validator instanceof DefaultIdentificationValidator))
                .filter(support)
                .findFirst()
                .orElse(defaultValidator);
    }
}
