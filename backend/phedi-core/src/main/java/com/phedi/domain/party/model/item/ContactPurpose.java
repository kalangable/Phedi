package com.phedi.domain.party.model.item;

/**
 * Finalidade/contexto de um contato do Party.
 *
 * Valores espelham o CHECK chk_party_contact_purpose da tabela party_contact.
 * NULL significa que não há finalidade declarada.
 */
public enum ContactPurpose {
    PERSONAL,
    COMMERCIAL,
    BILLING,
    SUPPORT,
    OTHER
}