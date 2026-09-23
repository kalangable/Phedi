package com.phedi.domain.party.model;

import lombok.Data;

@Data
public abstract class Party {

    private Identifier identifier;

    private Boolean isActive;

    private PartyType partyType;

    protected Party() {
    }

    protected Party(PartyType partyType) {
        this.partyType = partyType;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

}