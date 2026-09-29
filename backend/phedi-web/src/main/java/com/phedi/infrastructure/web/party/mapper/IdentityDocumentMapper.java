package com.phedi.infrastructure.web.party.mapper;

import java.time.LocalDate;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.IdentityDocumentType;
import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.infrastructure.web.party.dto.item.IdentityDocumentRequest;
import com.phedi.infrastructure.web.party.dto.item.IdentityDocumentResponse;
import com.phedi.infrastructure.web.party.dto.item.UpdateIdentityDocumentRequest;

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

    /**
     * Converte o patch em um domain para update.
     *
     * Os métodos {@code unwrap*} convertem {@code Optional.empty()} em
     * {@code null}, então campo ausente no corpo chega como null no domain.
     * O {@code updateEntity} da persistência é quem decide o que fazer com
     * esses null.
     *
     * O identificador vem do path ({@code /documents/{identifier}}), que é a
     * fonte autoritativa do recurso endereçado. O {@code identifier} do corpo
     * ({@code PartyItemUpdateRequest}) é ignorado: se divergirem, o path
     * prevalece.
     */
    @Mapping(target = "identifier", source = "identifier")
    PartyIdentityDocument toDomain(String identifier, UpdateIdentityDocumentRequest dto);


    @Mapping(target = "identifier", source = "identifier.value")
    IdentityDocumentResponse toDto(PartyIdentityDocument result);


    default Identifier mapStringToIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return null;
        }
        return new Identifier(identifier);
    }

    default String mapIdentifierToString(Identifier identifier) {
        return identifier == null ? null : identifier.value();
    }

    default String unwrapString(Optional<String> value) {
        return value.orElse(null);
    }

    default LocalDate unwrapLocalDate(Optional<LocalDate> value) {
        return value.orElse(null);
    }

    default Boolean unwrapBoolean(Optional<Boolean> value) {
        return value.orElse(null);
    }

    default IdentityDocumentType unwrapIdentityDocumentType(Optional<IdentityDocumentType> value) {
        return value.orElse(null);
    }

}
