package com.phedi.infrastructure.web.party.dto;

import com.phedi.domain.party.model.AddressType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AddressResponse extends PartyItemResponse {

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