package com.phedi.domain.party.model.item;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data 
@EqualsAndHashCode(callSuper = true) 
public class PartyContact extends PartyItem{

    private ContactType contactType;

    private ContactPurpose purpose;

    private String contactValue;

    private String countryCode;
}
