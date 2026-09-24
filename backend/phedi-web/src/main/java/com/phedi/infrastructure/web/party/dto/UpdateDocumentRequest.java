package com.phedi.infrastructure.web.party.dto;

import java.time.LocalDate;
import java.util.Optional;

import com.phedi.domain.party.model.DocumentType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateDocumentRequest extends PartyItemUpdateRequest {

    private Optional<DocumentType> documentType = Optional.empty();

    @Size(max = 100)
    private Optional<String> documentNumber = Optional.empty();

    @Size(min = 2, max = 2)
    private Optional<String> countryCode = Optional.empty();

    @Size(max = 10)
    private Optional<String> issuerRegion = Optional.empty();

    @Size(max = 100)
    private Optional<String> issuingAuthority = Optional.empty();

    private Optional<LocalDate> issuedAt = Optional.empty();

    private Optional<LocalDate> expiresAt = Optional.empty();

    /**
     * Consistência: validade, quando informada, deve ser posterior ou igual à
     * emissão.
     */
    @AssertTrue(message = "expiresAt deve ser posterior ou igual a issuedAt")
    public boolean validDateRange() {
        return issuedAt.isEmpty() || expiresAt.isEmpty()
                || !expiresAt.get().isBefore(issuedAt.get());
    }

}