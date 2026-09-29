package com.phedi.infrastructure.persistence.party.generator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.service.PublicIdGenerator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Gerador de identificador público baseado na sequence compartilhada
 * party_public_id_seq, usada por todas as tabelas da família Party.
 */
@Service
public class PublicIdSequenceGenerator implements PublicIdGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Identifier generate() {
        Number nextVal = (Number) entityManager
                .createNativeQuery("SELECT nextval('party_public_id_seq')")
                .getSingleResult();

        return new Identifier(nextVal.toString());
    }

}