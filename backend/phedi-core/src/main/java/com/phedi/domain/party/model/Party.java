package com.phedi.domain.party.model;

import lombok.Data;

@Data 
public abstract class Party {

    private PartyIdentifier partyIdentifier;

    private String identificationType;

    private String identificationNumber;

    private Boolean isActive;

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

}
