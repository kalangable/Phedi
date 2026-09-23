package com.phedi.infrastructure.persistence.party.mapper;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Party;
import com.phedi.infrastructure.persistence.party.entity.PartyEntity;

public interface PartyPersistenceMapper<D extends Party, E extends PartyEntity> extends PersistenceMapper<D, E> {

    default Identifier mapStringToIdentifier(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return new Identifier(value);
    }

}