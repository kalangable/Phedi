package com.phedi.infrastructure.web.party.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationResponse {

    private String partyIdentifier;

    private String identificationType;

    private String identificationNumber;

    private String legalName;

    private String tradeName;

    private String brandName;

    private LocalDate foundingDate;

    private Boolean isActive;

}