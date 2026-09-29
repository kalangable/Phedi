package com.phedi.infrastructure.web.party.dto.item;

import java.util.Optional;

import com.phedi.domain.party.model.item.ContactPurpose;
import com.phedi.domain.party.model.item.ContactType;

import jakarta.validation.constraints.Size;
import jakarta.validation.valueextraction.ExtractedValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateContactRequest extends PartyItemUpdateRequest {

    private Optional<ContactType> contactType = Optional.empty();

    private Optional<ContactPurpose> purpose = Optional.empty();

    private Optional<@ExtractedValue @Size(max = 255) String> contactValue = Optional.empty();

    private Optional<@ExtractedValue @Size(min = 2, max = 2) String> countryCode = Optional.empty();

}
