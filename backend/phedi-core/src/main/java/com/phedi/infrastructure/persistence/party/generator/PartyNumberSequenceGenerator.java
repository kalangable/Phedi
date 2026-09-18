package com.phedi.infrastructure.persistence.party.generator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phedi.domain.party.model.PartyIdentifier;
import com.phedi.domain.party.service.PartyIdentifierGenerator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service 
public class PartyNumberSequenceGenerator implements PartyIdentifierGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public PartyIdentifier generate() {
        Number nextVal = (Number) entityManager
                .createNativeQuery("SELECT nextval('party_number_seq')")
                .getSingleResult();

        return new PartyIdentifier(nextVal.toString());
    }

}
