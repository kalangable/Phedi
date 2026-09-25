package com.phedi.domain.party.model.item;

import com.phedi.domain.party.model.PartyBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) 
public abstract class PartyItem extends PartyBase {

    private Boolean primary;

    public void markAsPrimary() {
        this.primary = true;
    }

    public void demoteToSecondary() {
        this.primary = false;
    }
}