package com.phedi.infrastructure.web.party.dto;

import java.time.LocalDate;
import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
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

    public Optional<String> getLegalName() {
        return Optional.ofNullable(legalName);
    }

    public Optional<String> getTradeName() {
        return Optional.ofNullable(tradeName);
    }

    public Optional<String> getBrandName() {
        return Optional.ofNullable(brandName);
    }

    public Optional<LocalDate> getFoundingDate() {
        return Optional.ofNullable(foundingDate);
    }

}
