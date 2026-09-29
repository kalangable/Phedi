package com.phedi.infrastructure.web.party.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.phedi.domain.party.model.Organization;
import com.phedi.infrastructure.web.party.dto.CreateOrganizationRequest;
import com.phedi.infrastructure.web.party.dto.OrganizationResponse;
import com.phedi.infrastructure.web.party.dto.UpdateOrganizationRequest;

@Mapper(componentModel = "spring", uses = {PartyConversions.class, IdentityDocumentMapper.class})
public interface OrganizationMapper {

    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Organization toDomain(CreateOrganizationRequest dto);

    @Mapping(target = "identifier", source = "identifier")
    Organization toDomain(String identifier, UpdateOrganizationRequest dto);

    OrganizationResponse toDto(Organization domain);

}
