package com.phedi.infrastructure.web.party.dto.item;

import java.time.LocalDate;
import java.util.Optional;

import com.phedi.domain.party.model.item.IdentityDocumentType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import jakarta.validation.valueextraction.ExtractedValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateIdentityDocumentRequest extends PartyItemUpdateRequest {

    private Optional<IdentityDocumentType> identityDocumentType = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 100) String> identityDocumentNumber = Optional.empty();

    private Optional<@ExtractedValue @Size(min = 2, max = 2) String> countryCode = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 10) String> issuerRegion = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 100) String> issuingAuthority = Optional.empty();

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
