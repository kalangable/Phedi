package com.phedi.infrastructure.web.party.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.infrastructure.web.party.dto.item.IdentityDocumentRequest;
import com.phedi.infrastructure.web.party.dto.item.IdentityDocumentResponse;
import com.phedi.infrastructure.web.party.dto.item.UpdateIdentityDocumentRequest;

@Mapper(componentModel = "spring", uses = PartyConversions.class)
public interface IdentityDocumentMapper {

    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    PartyIdentityDocument toDomain(IdentityDocumentRequest request);

    @Mapping(target = "identifier", source = "identifier")
    PartyIdentityDocument toDomain(String identifier, UpdateIdentityDocumentRequest dto);

    IdentityDocumentResponse toDto(PartyIdentityDocument result);

}
