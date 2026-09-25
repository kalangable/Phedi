package com.phedi.infrastructure.persistence.party.mapper.item;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.PartyItem;
import com.phedi.infrastructure.persistence.party.entity.item.PartyItemEntity;
import com.phedi.infrastructure.persistence.party.mapper.PersistenceMapper;

public interface PartyItemPersistenceMapper<DOMAIN extends PartyItem, ENTITY extends PartyItemEntity> extends PersistenceMapper<DOMAIN, ENTITY> {

    default Identifier mapStringToIdentifier(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return new Identifier(value);
    }

}