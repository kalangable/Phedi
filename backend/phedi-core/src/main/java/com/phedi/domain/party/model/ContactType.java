package com.phedi.domain.party.model;

/**
 * Canal de contato do Party.
 *
 * Valores espelham o CHECK chk_party_contact_type da tabela party_contact.
 */
public enum ContactType {
    EMAIL,
    PHONE,
    MOBILE,
    LANDLINE,
    WHATSAPP,
    FAX,
    WEBSITE,
    OTHER
}