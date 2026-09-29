package com.phedi.infrastructure.persistence.party.mapper;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Party;
import com.phedi.infrastructure.persistence.party.entity.PartyEntity;

public interface PartyPersistenceMapper<DOMAIN extends Party, ENTITY extends PartyEntity> extends PersistenceMapper<DOMAIN, ENTITY> {

    default Identifier mapStringToIdentifier(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return new Identifier(value);
    }

}