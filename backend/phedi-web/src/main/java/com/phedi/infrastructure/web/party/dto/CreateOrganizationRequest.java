package com.phedi.infrastructure.web.party.dto;

import java.time.LocalDate;
import java.util.List;

import com.phedi.infrastructure.web.party.dto.item.AddressRequest;
import com.phedi.infrastructure.web.party.dto.item.ContactRequest;
import com.phedi.infrastructure.web.party.dto.item.DocumentRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrganizationRequest {

    @NotBlank
    @Size(max = 200)
    private String legalName;

    @Size(max = 200)
    private String tradeName;

    @Size(max = 200)
    private String brandName;

    private LocalDate foundingDate;

    @Valid
    private List<ContactRequest> contacts;

    @Valid
    private List<AddressRequest> addresses;

    @Valid
    @NotEmpty
    private List<DocumentRequest> documents;

}
