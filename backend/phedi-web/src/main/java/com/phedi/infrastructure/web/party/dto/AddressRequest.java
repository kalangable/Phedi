package com.phedi.infrastructure.web.party.dto;

import com.phedi.domain.party.model.AddressType;

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
public class AddressRequest extends PartyItemRequest {

    @NotNull
    private AddressType addressType;

    @Size(max = 50)
    private String label;

    @NotBlank
    @Size(max = 255)
    private String street;

    @Size(max = 20)
    private String number;

    @Size(max = 100)
    private String complement;

    @Size(max = 100)
    private String district;

    @NotBlank
    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String stateRegion;

    @Size(max = 20)
    private String postalCode;

    @NotBlank
    @Size(min = 2, max = 2)
    private String countryCode;

}