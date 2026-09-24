package com.phedi.domain.party.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data 
@EqualsAndHashCode(callSuper = true)
public class PartyAddress extends PartyItem{

    private AddressType addressType;

    private String label;

    private String street;

    private String number;

    private String complement;

    private String district;

    private String city;

    private String stateRegion;

    private String postalCode;

    private String countryCode;

}
