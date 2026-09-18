package com.phedi.application.party.organization;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phedi.domain.party.exception.DuplicateIdentificationException;
import com.phedi.domain.party.exception.InvalidIdentificationException;
import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.model.PartyIdentifier;
import com.phedi.domain.party.repository.OrganizationRepository;
import com.phedi.domain.party.service.PartyIdentifierGenerator;
import com.phedi.domain.party.validation.IdentificationValidationService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationService implements OrganizationCreationService, OrganizationUpdateService,
        OrganizationDeletionService, OrganizationQueryService, OrganizationStatusChangeService {

    private final OrganizationRepository organizationRepository;
    private final PartyIdentifierGenerator partyIdentifierGenerator;
    private final IdentificationValidationService validationService;

    @Override
    public Organization create(Organization organization) {

        validateIdentification(organization.getIdentificationType(), organization.getIdentificationNumber());
        checkDuplicateIdentification(organization.getIdentificationType(), organization.getIdentificationNumber());

        organization.setPartyIdentifier(partyIdentifierGenerator.generate());
        return organizationRepository.save(organization);
    }

    @Override
    @Transactional(readOnly = true)
    public Organization findById(PartyIdentifier partyIdentifier) {
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
        Organization existingOrganization = findById(updatedOrganization.getPartyIdentifier());

        // Validate new identification if changed
        if (updatedOrganization.getIdentificationType() != null &&
                updatedOrganization.getIdentificationNumber() != null) {

            boolean identificationChanged = !updatedOrganization.getIdentificationType()
                    .equals(existingOrganization.getIdentificationType()) ||
                    !updatedOrganization.getIdentificationNumber()
                            .equals(existingOrganization.getIdentificationNumber());

            if (identificationChanged) {
                validateIdentification(updatedOrganization.getIdentificationType(),
                        updatedOrganization.getIdentificationNumber());
                checkDuplicateIdentification(updatedOrganization.getIdentificationType(),
                        updatedOrganization.getIdentificationNumber());
            }

        }

        // Update fields
        existingOrganization.setLegalName(updatedOrganization.getLegalName());
        existingOrganization.setTradeName(updatedOrganization.getTradeName());
        existingOrganization.setBrandName(updatedOrganization.getBrandName());
        existingOrganization.setFoundingDate(updatedOrganization.getFoundingDate());
        existingOrganization.setIdentificationType(updatedOrganization.getIdentificationType());
        existingOrganization.setIdentificationNumber(updatedOrganization.getIdentificationNumber());
        existingOrganization.setIsActive(updatedOrganization.getIsActive());

        return organizationRepository.save(existingOrganization);
    }

    @Override
    public void delete(PartyIdentifier partyIdentifier) {
        organizationRepository.deleteByPartyIdentifier(partyIdentifier);
    }

    @Override
    public void activate(PartyIdentifier partyIdentifier) {
        organizationRepository.deactivate(partyIdentifier.value());
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
