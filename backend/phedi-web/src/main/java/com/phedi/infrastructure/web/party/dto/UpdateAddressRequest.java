package com.phedi.infrastructure.web.party.dto;

import java.util.Optional;

import com.phedi.domain.party.model.AddressType;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateAddressRequest extends PartyItemUpdateRequest {

    private Optional<AddressType> addressType = Optional.empty();

    @Size(max = 50)
    private Optional<String> label = Optional.empty();

    @Size(max = 255)
    private Optional<String> street = Optional.empty();

    @Size(max = 20)
    private Optional<String> number = Optional.empty();

    @Size(max = 100)
    private Optional<String> complement = Optional.empty();

    @Size(max = 100)
    private Optional<String> district = Optional.empty();

    @Size(max = 100)
    private Optional<String> city = Optional.empty();

    @Size(max = 100)
    private Optional<String> stateRegion = Optional.empty();

    @Size(max = 20)
    private Optional<String> postalCode = Optional.empty();

    @Size(min = 2, max = 2)
    private Optional<String> countryCode = Optional.empty();

}