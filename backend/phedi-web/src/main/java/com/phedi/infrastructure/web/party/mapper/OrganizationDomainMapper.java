package com.phedi.infrastructure.web.party.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.model.PartyIdentifier;
import com.phedi.infrastructure.web.party.dto.CreateOrganizationRequest;
import com.phedi.infrastructure.web.party.dto.OrganizationResponse;
import com.phedi.infrastructure.web.party.dto.UpdateOrganizationRequest;

@Mapper(componentModel = "spring")
public interface OrganizationDomainMapper {

    @Mapping(target = "partyIdentifier", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Organization toDomain(CreateOrganizationRequest dto);

    @Mapping(target = "partyIdentifier", source = "partyIdentifier")
    Organization toDomain(String partyIdentifier, UpdateOrganizationRequest dto);

    @Mapping(target = "partyIdentifier", source = "partyIdentifier.value")
    OrganizationResponse toDto(Organization domain);

    default PartyIdentifier mapStringToPartyIdentifier(String partyIdentifier) {
        if (partyIdentifier == null || partyIdentifier.isBlank()) {
            return null;
        }
        return new PartyIdentifier(partyIdentifier);
    }
}