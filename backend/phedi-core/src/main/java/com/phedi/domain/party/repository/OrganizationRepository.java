package com.phedi.domain.party.repository;

import java.util.List;
import java.util.Optional;

import com.phedi.domain.party.model.Organization;
public interface OrganizationRepository extends PartyRepository<Organization>{

    // Find by legal name
    List<Organization> findByLegalName(String legalName);

    // Find by trade name
    List<Organization> findByTradeName(String tradeName);

    Boolean existsByIdentificationTypeAndIdentificationNumber(String identificationType, String identificationNumber);

    Optional<Organization> findByIdentification(String identificationType, String identificationNumber);
}
