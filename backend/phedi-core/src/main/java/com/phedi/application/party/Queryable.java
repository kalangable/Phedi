package com.phedi.application.party;

import java.util.List;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.PartyBase;

public interface Queryable<DOMAIN extends PartyBase> {

    DOMAIN findByIdentifier(Identifier identifier);

    List<DOMAIN> findAll();

}
