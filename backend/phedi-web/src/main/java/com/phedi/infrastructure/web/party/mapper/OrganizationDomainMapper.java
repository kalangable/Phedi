package com.phedi.infrastructure.web.party.mapper;

import java.time.LocalDate;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Organization;
import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.infrastructure.web.party.dto.CreateOrganizationRequest;
import com.phedi.infrastructure.web.party.dto.item.DocumentRequest;
import com.phedi.infrastructure.web.party.dto.OrganizationResponse;
import com.phedi.infrastructure.web.party.dto.UpdateOrganizationRequest;

@Mapper(componentModel = "spring")
public interface OrganizationDomainMapper {

    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Organization toDomain(CreateOrganizationRequest dto);

    @Mapping(target = "identifier", source = "identifier")
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Organization toDomain(String identifier, UpdateOrganizationRequest dto);

    @Mapping(target = "identifier", source = "identifier.value")
    OrganizationResponse toDto(Organization domain);

    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    PartyIdentityDocument toDomain(DocumentRequest request);

    default Identifier mapStringToIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return null;
        }
        return new Identifier(identifier);
    }

    default String mapIdentifierToString(Identifier identifier) {
        return identifier == null ? null : identifier.value();
    }

    /**
     * Interino: unwrap dos campos opcionais do Update enquanto o mapper do
     * agregado (com regras de seção/primary/identidade) não é construído.
     */
    default String unwrapString(Optional<String> value) {
        return value.orElse(null);
    }

    default LocalDate unwrapLocalDate(Optional<LocalDate> value) {
        return value.orElse(null);
    }
}
