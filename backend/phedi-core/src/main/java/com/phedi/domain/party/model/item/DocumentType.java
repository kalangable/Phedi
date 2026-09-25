package com.phedi.domain.party.model.item;

/**
 * Tipo de documento de identificação do Party.
 *
 * Exemplos: CPF, CNPJ (BR); SSN, EIN (US); RG, PASSPORT;
 * VAT, STATE_TAX_REGISTRATION, etc.
 */
public enum DocumentType {
    CPF,
    CNPJ,
    RG,
    PASSPORT,
    STATE_TAX_REGISTRATION,
    VAT,
    SSN,
    EIN,
    OTHER
}