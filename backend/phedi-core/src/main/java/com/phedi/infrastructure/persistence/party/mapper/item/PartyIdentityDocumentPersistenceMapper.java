package com.phedi.infrastructure.persistence.party.mapper.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.infrastructure.persistence.party.entity.item.PartyIdentityDocumentEntity;

@Mapper(componentModel = "spring")
public interface PartyIdentityDocumentPersistenceMapper extends PartyItemPersistenceMapper<PartyIdentityDocument, PartyIdentityDocumentEntity> {

    @Override
    @Mapping(target = "publicId", source = "identifier.value")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "party", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "primary", defaultValue = "false")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    PartyIdentityDocumentEntity toEntity(PartyIdentityDocument domain);

    @Override
    @Mapping(target = "identifier", source = "publicId")
    PartyIdentityDocument toDomain(PartyIdentityDocumentEntity entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "party", ignore = true)
    @Mapping(target = "primary", defaultValue = "false")
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    void updateEntity(PartyIdentityDocument domain, @MappingTarget PartyIdentityDocumentEntity entity);

}