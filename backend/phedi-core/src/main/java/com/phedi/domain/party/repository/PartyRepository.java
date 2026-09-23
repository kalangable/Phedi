package com.phedi.domain.party.repository;

import java.util.List;
import java.util.Optional;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.Party;

public interface PartyRepository<DOMAIN extends Party> {

    DOMAIN insert(DOMAIN domain);

    DOMAIN update(DOMAIN domain);

    void deleteByIdentifier(Identifier identifier);

    Optional<DOMAIN> findByIdentifier(Identifier identifier);

    List<DOMAIN> findAll();

    void activate(Identifier identifier);

    void deactivate(Identifier identifier);

}
