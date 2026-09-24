package com.phedi.domain.party.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PartyIdentityDocument extends PartyItem {

    private DocumentType documentType;

    private String documentNumber;

    private String countryCode;

    private String issuerRegion;

    private String issuingAuthority;

    private LocalDate issuedAt;

    private LocalDate expiresAt;

}
