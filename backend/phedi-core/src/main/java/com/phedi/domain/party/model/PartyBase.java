package com.phedi.domain.party.model;

import lombok.Data;

@Data
public abstract class PartyBase {

    private Identifier identifier;

    private Boolean isActive;

    protected PartyBase() {
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
