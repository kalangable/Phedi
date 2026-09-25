package com.phedi.domain.party.model;

import java.util.ArrayList;
import java.util.List;

import com.phedi.domain.party.model.item.PartyAddress;
import com.phedi.domain.party.model.item.PartyContact;
import com.phedi.domain.party.model.item.PartyIdentityDocument;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Party extends PartyBase {

    private PartyType partyType;

    private List<PartyContact> contacts = new ArrayList<>();

    private List<PartyAddress> addresses = new ArrayList<>();

    private List<PartyIdentityDocument> documents = new ArrayList<>();

    protected Party() {
    }

    protected Party(PartyType partyType) {
        this.partyType = partyType;
    }

}