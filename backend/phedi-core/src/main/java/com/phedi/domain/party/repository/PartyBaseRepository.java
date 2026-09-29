package com.phedi.domain.party.repository;

import java.util.Optional;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.PartyBase;

public interface PartyBaseRepository<DOMAIN extends PartyBase> {
    
    Optional<DOMAIN> findByIdentifier(Identifier identifier);

    DOMAIN update(DOMAIN domain);

    void deleteByIdentifier(Identifier identifier);

    void activate(Identifier identifier);

    void deactivate(Identifier identifier);
}
