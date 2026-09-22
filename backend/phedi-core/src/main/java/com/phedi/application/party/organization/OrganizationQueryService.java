package com.phedi.application.party.organization;

import java.util.List;

import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.model.PartyIdentifier;

public interface OrganizationQueryService {

    Organization findByIdentifier(PartyIdentifier partyIdentifier);

    List<Organization> findAll();

    Organization findByIdentification(String identificationType, String identificationNumber);

}
