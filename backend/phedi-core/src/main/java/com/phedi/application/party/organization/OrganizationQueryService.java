package com.phedi.application.party.organization;

import java.util.List;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Organization;

public interface OrganizationQueryService {

    Organization findByIdentifier(Identifier identifier);

    List<Organization> findAll();

    Organization findByIdentification(String identificationType, String identificationNumber);

}
