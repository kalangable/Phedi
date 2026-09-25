package com.phedi.infrastructure.web.party.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.phedi.infrastructure.web.party.dto.item.UpdateAddressRequest;
import com.phedi.infrastructure.web.party.dto.item.UpdateContactRequest;
import com.phedi.infrastructure.web.party.dto.item.UpdateDocumentRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload parcial para PATCH: campos ausentes (Optional.empty) não são
 * alterados. Uma seção (contacts/addresses/documents) que não vier no JSON
 * (null) não é alterada; uma lista vazia presente esvazia a seção; uma lista
 * presente substitui a lista inteira.
 */
@Data
@NoArgsConstructor
public class UpdateOrganizationRequest {

    @Size(max = 200)
    private Optional<String> legalName = Optional.empty();

    @Size(max = 200)
    private Optional<String> tradeName = Optional.empty();

    @Size(max = 200)
    private Optional<String> brandName = Optional.empty();

    private Optional<LocalDate> foundingDate = Optional.empty();

    @Valid
    private List<UpdateContactRequest> contacts;

    @Valid
    private List<UpdateAddressRequest> addresses;

    @Valid
    private List<UpdateDocumentRequest> documents;

}