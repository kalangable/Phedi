package com.phedi.application.party;

import com.phedi.domain.party.model.PartyBase;

public interface Updatable<DOMAIN extends PartyBase> {

    DOMAIN update(DOMAIN object);

}
