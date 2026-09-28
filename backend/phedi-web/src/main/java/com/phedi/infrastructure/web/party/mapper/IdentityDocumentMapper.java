package com.phedi.infrastructure.web.party.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.infrastructure.web.party.dto.item.IdentityDocumentRequest;

/**
 * Conversão entre o payload de documento de identificação e o modelo de
 * domínio {@link PartyIdentityDocument}.
 *
 * Separado de {@link OrganizationDomainMapper} porque o documento é um item
 * do agregado Party, com ciclo de vida próprio, e não um atributo da
 * organização.
 */
@Mapper(componentModel = "spring")
public interface IdentityDocumentMapper {

    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    PartyIdentityDocument toDomain(IdentityDocumentRequest request);

}
