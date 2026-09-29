package com.phedi.infrastructure.web.party.mapper;

import java.time.LocalDate;
import java.util.Optional;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.IdentityDocumentType;

public interface PartyConversions {

    static Identifier mapStringToIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return null;
        }
        return new Identifier(identifier);
    }

    static String mapIdentifierToString(Identifier identifier) {
        return identifier == null ? null : identifier.value();
    }

    static String unwrapString(Optional<String> value) {
        return value.orElse(null);
    }

    static LocalDate unwrapLocalDate(Optional<LocalDate> value) {
        return value.orElse(null);
    }

    static Boolean unwrapBoolean(Optional<Boolean> value) {
        return value.orElse(null);
    }

    static IdentityDocumentType unwrapIdentityDocumentType(Optional<IdentityDocumentType> value) {
        return value.orElse(null);
    }

}
