package com.phedi.infrastructure.web.party.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Organization;
import com.phedi.infrastructure.web.party.dto.CreateOrganizationRequest;
import com.phedi.infrastructure.web.party.dto.OrganizationResponse;
import com.phedi.infrastructure.web.party.dto.UpdateOrganizationRequest;

@Mapper(componentModel = "spring")
public interface OrganizationDomainMapper {

    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Organization toDomain(CreateOrganizationRequest dto);

    @Mapping(target = "identifier", source = "identifier")
    Organization toDomain(String identifier, UpdateOrganizationRequest dto);

    @Mapping(target = "identifier", source = "identifier.value")
    OrganizationResponse toDto(Organization domain);

    default Identifier mapStringToIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return null;
        }
        return new Identifier(identifier);
    }
}
