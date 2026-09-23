package com.phedi.domain.party.service;

import com.phedi.domain.party.model.Identifier;

/**
 * Gera o próximo identificador público da família Party.
 *
 * Todos os registros (party, party_identity_document,
 * party_contact e party_address) consomem a mesma sequence
 * compartilhada, portanto um único gerador atende a agregado inteiro.
 */
public interface PublicIdGenerator {

    Identifier generate();
}