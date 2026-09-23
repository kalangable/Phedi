package com.phedi.domain.party.model;

import lombok.Data;

@Data 
public abstract class Party {

    private Identifier identifier;

    private Boolean isActive;

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

}
