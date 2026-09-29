package com.phedi.application.party;

import com.phedi.domain.party.model.PartyBase;

public interface Creatable <DOMAIN extends PartyBase>{

    DOMAIN create(DOMAIN domain);
    
}
