package com.phedi.application.party.organization;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phedi.domain.party.exception.DuplicateIdentificationException;
import com.phedi.domain.party.exception.InvalidIdentificationException;
import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.model.PartyIdentifier;
import com.phedi.domain.party.repository.OrganizationRepository;
import com.phedi.domain.party.validation.IdentificationValidationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationService implements OrganizationCreationService, OrganizationUpdateService,
        OrganizationDeletionService, OrganizationQueryService, OrganizationStatusChangeService {

    private final OrganizationRepository organizationRepository;
    private final IdentificationValidationService validationService;

    @Override
    public Organization create(Organization organization) {

        validateIdentification(organization.getIdentificationType(), organization.getIdentificationNumber());
        checkDuplicateIdentification(organization.getIdentificationType(), organization.getIdentificationNumber());

        return organizationRepository.insert(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public Organization findByIdentifier(PartyIdentifier partyIdentifier) {
        return organizationRepository.findByPartyIdentifier(partyIdentifier)
                .orElseThrow(() -> new RuntimeException(String.format("Organization not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Organization findByIdentification(String identificationType, String identificationNumber) {
        return organizationRepository.findByIdentification(identificationType, identificationNumber)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Organization %s with %s not found", identificationType, identificationNumber)));
    }

    @Override
    public Organization update(Organization updatedOrganization) {

        Organization existingOrganization = findByIdentifier(updatedOrganization.getPartyIdentifier());

        // Validate new identification if changed
        checkUpdateRoles(updatedOrganization, existingOrganization);

        return organizationRepository.update(updatedOrganization);
    }

    protected void checkUpdateRoles(Organization updatedOrganization, Organization existingOrganization) {

        log.info("Compare Organizations [{}] [{}]", updatedOrganization, existingOrganization);
        if (updatedOrganization.getIdentificationType() != null &&
                updatedOrganization.getIdentificationNumber() != null) {

            boolean identificationChanged = !updatedOrganization.getIdentificationType().equals(existingOrganization.getIdentificationType()) 
                || !updatedOrganization.getIdentificationNumber().equals(existingOrganization.getIdentificationNumber());

            if (identificationChanged) {
                validateIdentification(updatedOrganization.getIdentificationType(), updatedOrganization.getIdentificationNumber());
                checkDuplicateIdentification(updatedOrganization.getIdentificationType(), updatedOrganization.getIdentificationNumber());
            }
        }
    }

    @Override
    public void delete(PartyIdentifier partyIdentifier) {
        organizationRepository.deleteByPartyIdentifier(partyIdentifier);
    }

    @Override
    public void activate(PartyIdentifier partyIdentifier) {
        organizationRepository.activate(partyIdentifier.value());
    }

    @Override
    public void deactivate(PartyIdentifier partyIdentifier) {
        organizationRepository.deactivate(partyIdentifier.value());
    }

    private void validateIdentification(String identificationType, String identificationNumber) {
        if (!validationService.validate(identificationType, identificationNumber)) {
            throw new InvalidIdentificationException(identificationType, identificationNumber);
        }
    }

    private void checkDuplicateIdentification(String identificationType, String identificationNumber) {
        if (organizationRepository.existsByIdentificationTypeAndIdentificationNumber(identificationType,
                identificationNumber)) {
            throw new DuplicateIdentificationException(identificationType, identificationNumber);
        }
    }
}
