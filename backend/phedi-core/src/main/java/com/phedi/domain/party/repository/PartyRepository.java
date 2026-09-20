package com.phedi.domain.party.repository;

import java.util.List;
import java.util.Optional;

import com.phedi.domain.party.model.Party;
import com.phedi.domain.party.model.PartyIdentifier;

public interface PartyRepository<E extends Party> {

    E save(E domain);

    void deleteByPartyIdentifier(PartyIdentifier partyIdentifier);

    Optional<E> findByPartyIdentifier(PartyIdentifier partyIdentifier);

    List<E> findAll();

    void activate(String partyNumber);

    void deactivate(String partyNumber);

}
