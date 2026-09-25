package com.phedi.infrastructure.web.party.dto.item;

import java.time.LocalDate;

import com.phedi.domain.party.model.item.DocumentType;

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
public class DocumentRequest extends PartyItemRequest {

    @NotNull
    private DocumentType documentType;

    @NotBlank
    @Size(max = 100)
    private String documentNumber;

    @Size(min = 2, max = 2)
    private String countryCode;

    @Size(max = 10)
    private String issuerRegion;

    @Size(max = 100)
    private String issuingAuthority;

    private LocalDate issuedAt;

    private LocalDate expiresAt;

    /**
     * Validação de consistência: a data de validade, quando informada,
     * deve ser posterior (ou igual) à data de emissão.
     */
    @AssertTrue(message = "expiresAt deve ser posterior ou igual a issuedAt")
    public boolean validDateRange() {
        return issuedAt == null || expiresAt == null || !expiresAt.isBefore(issuedAt);
    }

}