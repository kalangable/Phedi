package com.phedi.domain.party.repository;

import java.util.List;
import java.util.Optional;

import com.phedi.domain.party.model.Party;
import com.phedi.domain.party.model.PartyIdentifier;

public interface PartyRepository<DOMAIN extends Party> {

    DOMAIN insert(DOMAIN domain);

    DOMAIN update(DOMAIN domain);

    void deleteByPartyIdentifier(PartyIdentifier partyIdentifier);

    Optional<DOMAIN> findByPartyIdentifier(PartyIdentifier partyIdentifier);

    List<DOMAIN> findAll();

    void activate(String partyNumber);

    void deactivate(String partyNumber);

}
