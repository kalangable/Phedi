package com.phedi.application.party;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.PartyBase;

public interface Findable<DOMAIN extends PartyBase> {

    DOMAIN findByIdentifier(Identifier identifier);

}
