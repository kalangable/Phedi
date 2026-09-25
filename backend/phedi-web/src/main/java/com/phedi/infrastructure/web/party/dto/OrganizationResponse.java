package com.phedi.infrastructure.web.party.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phedi.infrastructure.web.party.dto.item.AddressResponse;
import com.phedi.infrastructure.web.party.dto.item.ContactResponse;
import com.phedi.infrastructure.web.party.dto.item.DocumentResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrganizationResponse {

    private String identifier;

    private String legalName;

    private String tradeName;

    private String brandName;

    private LocalDate foundingDate;

    private Boolean isActive;

    private List<ContactResponse> contacts;

    private List<AddressResponse> addresses;

    private List<DocumentResponse> documents;

}