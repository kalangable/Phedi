package com.phedi.domain.party.model.item;

/**
 * Tipo de endereço do Party.
 *
 * Valores espelham o CHECK chk_party_address_type da tabela party_address.
 */
public enum AddressType {
    RESIDENTIAL,
    COMMERCIAL,
    BILLING,
    SHIPPING,
    CORRESPONDENCE,
    OTHER
}