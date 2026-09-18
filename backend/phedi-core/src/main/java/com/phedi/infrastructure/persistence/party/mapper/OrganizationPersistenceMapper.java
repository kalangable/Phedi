package com.phedi.infrastructure.persistence.party.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.phedi.domain.party.model.Organization;
import com.phedi.infrastructure.persistence.party.entity.OrganizationEntity;

@Mapper(componentModel = "spring")
public interface OrganizationPersistenceMapper extends PartyPersistenceMapper<Organization, OrganizationEntity> {

    @Override
    @Mapping(target = "partyNumber", source = "partyIdentifier.value")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "partyType", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrganizationEntity toEntity(Organization domain);

    @Override
    @Mapping(target = "partyIdentifier", source = "partyNumber")
    Organization toDomain(OrganizationEntity entity);

}
