package com.phedi.infrastructure.web.party.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrganizationRequest {

    @NotBlank
    @Size(max = 200)
    private String legalName;

    @Size(max = 200)
    private String tradeName;

    @Size(max = 200)
    private String brandName;

    private LocalDate foundingDate;

    @NotBlank
    @Size(max = 20)
    private String identificationType;

    @NotBlank
    @Size(max = 50)
    private String identificationNumber;

}