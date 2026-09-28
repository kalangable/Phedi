package com.phedi.domain.party.model.item;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PartyIdentityDocument extends PartyItem {

    private IdentityDocumentType identityDocumentType;

    private String identityDocumentNumber;

    private String countryCode;

    private String issuerRegion;

    private String issuingAuthority;

    private LocalDate issuedAt;

    private LocalDate expiresAt;

}
