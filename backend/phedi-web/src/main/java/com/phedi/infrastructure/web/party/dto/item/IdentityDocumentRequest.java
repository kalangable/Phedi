package com.phedi.infrastructure.web.party.dto.item;

import java.time.LocalDate;
import java.util.Optional;

import com.phedi.domain.party.model.item.IdentityDocumentType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class IdentityDocumentRequest extends PartyItemRequest {

    @NotNull
    private IdentityDocumentType identityDocumentType;

    @NotBlank
    @Size(max = 100)
    private String identityDocumentNumber;

    @Size(min = 2, max = 2)
    private String countryCode;

    @Size(max = 10)
    private String issuerRegion;

    @Size(max = 100)
    private String issuingAuthority;

    private LocalDate issuedAt;

    private LocalDate expiresAt;

    public Optional<IdentityDocumentType> getIdentityDocumentType() {
        return Optional.ofNullable(identityDocumentType);
    }

    public Optional<String> getCountryCode() {
        return Optional.ofNullable(countryCode);
    }

    public Optional<String> getIdentityDocumentNumber() {
        return Optional.ofNullable(identityDocumentNumber);
    }

    public Optional<String> getIssuerRegion() {
        return Optional.ofNullable(issuerRegion);
    }

    public Optional<String> getIssuingAuthority() {
        return Optional.ofNullable(issuingAuthority);
    }

    public Optional<LocalDate> getIssuedAt() {
        return Optional.ofNullable(issuedAt);
    }

    public Optional<LocalDate> getExpiresAt() {
        return Optional.ofNullable(expiresAt);
    }


}
