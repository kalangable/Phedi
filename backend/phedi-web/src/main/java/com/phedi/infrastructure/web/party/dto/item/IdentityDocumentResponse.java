package com.phedi.infrastructure.web.party.dto.item;

import java.time.LocalDate;

import com.phedi.domain.party.model.item.IdentityDocumentType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class IdentityDocumentResponse extends PartyItemResponse {

    private IdentityDocumentType identityDocumentType;

    private String identityDocumentNumber;

    private String countryCode;

    private String issuerRegion;

    private String issuingAuthority;

    private LocalDate issuedAt;

    private LocalDate expiresAt;

}
