package com.phedi.infrastructure.web.party.dto.item;

import java.util.Optional;

import com.phedi.domain.party.model.item.AddressType;

import jakarta.validation.constraints.Size;
import jakarta.validation.valueextraction.ExtractedValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateAddressRequest extends PartyItemUpdateRequest {

    private Optional<AddressType> addressType = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 50) String> label = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 255) String> street = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 20) String> number = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 100) String> complement = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 100) String> district = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 100) String> city = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 100) String> stateRegion = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 20) String> postalCode = Optional.empty();

    private Optional<@ExtractedValue @Size(min = 2, max = 2) String> countryCode = Optional.empty();

}
