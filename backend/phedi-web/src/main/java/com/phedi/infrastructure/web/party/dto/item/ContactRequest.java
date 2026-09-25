package com.phedi.infrastructure.web.party.dto.item;

import com.phedi.domain.party.model.item.ContactPurpose;
import com.phedi.domain.party.model.item.ContactType;

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
public class ContactRequest extends PartyItemRequest {

    @NotNull
    private ContactType contactType;

    private ContactPurpose purpose;

    @NotBlank
    @Size(max = 255)
    private String contactValue;

    @Size(min = 2, max = 2)
    private String countryCode;

}