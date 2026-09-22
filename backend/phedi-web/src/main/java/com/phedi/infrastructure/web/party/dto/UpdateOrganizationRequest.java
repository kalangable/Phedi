package com.phedi.infrastructure.web.party.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor 
public class UpdateOrganizationRequest {

    @Size(max = 200)
    private String legalName;

    @Size(max = 200)
    private String tradeName;

    @Size(max = 200)
    private String brandName;

    private LocalDate foundingDate;

    @Size(max = 20)
    private String identificationType;

    @Size(max = 50)
    private String identificationNumber;

}