package com.phedi.infrastructure.web.party.dto;

import com.phedi.domain.party.model.ContactPurpose;
import com.phedi.domain.party.model.ContactType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ContactResponse extends PartyItemResponse {

    private ContactType contactType;

    private ContactPurpose purpose;

    private String contactValue;

    private String countryCode;

}