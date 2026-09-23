package com.phedi.domain.party.model;

import java.util.Set;

/**
 * Tipo da Party: pessoa física ou organização.
 *
 * Carrega as regras de negócio de quais tipos de documento e
 * endereço são válidos para cada tipo de Party.
 */
public enum PartyType {
    PERSON,
    ORGANIZATION;

    /**
     * Documentos de identificação válidos para o tipo de Party.
     *
     * Pessoa física (PF): documentos individuais (CPF, RG, passaporte, SSN).
     * Organização (PJ): registros de pessoa jurídica (CNPJ, EIN, ...).
     */
    public Set<DocumentType> allowedDocumentTypes() {
        return this == PERSON
                ? Set.of(DocumentType.CPF, DocumentType.RG, DocumentType.PASSPORT,
                        DocumentType.SSN, DocumentType.OTHER)
                : Set.of(DocumentType.CNPJ, DocumentType.EIN,
                        DocumentType.STATE_TAX_REGISTRATION, DocumentType.VAT, DocumentType.OTHER);
    }

    /**
     * Tipos de endereço válidos para o tipo de Party.
     *
     * Endereço comercial (COMMERCIAL) não faz sentido para PF, assim como
     * endereço residencial (RESIDENTIAL) não faz sentido para PJ.
     */
    public Set<AddressType> allowedAddressTypes() {
        return this == PERSON
                ? Set.of(AddressType.RESIDENTIAL, AddressType.BILLING,
                        AddressType.SHIPPING, AddressType.CORRESPONDENCE, AddressType.OTHER)
                : Set.of(AddressType.COMMERCIAL, AddressType.BILLING,
                        AddressType.SHIPPING, AddressType.CORRESPONDENCE, AddressType.OTHER);
    }

    public boolean supports(DocumentType documentType) {
        return allowedDocumentTypes().contains(documentType);
    }

    public boolean supports(AddressType addressType) {
        return allowedAddressTypes().contains(addressType);
    }
}