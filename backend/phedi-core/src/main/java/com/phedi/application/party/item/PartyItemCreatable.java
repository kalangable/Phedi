package com.phedi.application.party.item;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.PartyItem;

public interface PartyItemCreatable<DOMAIN extends PartyItem> {

    DOMAIN createItem(Identifier partyIdentifier, DOMAIN domain);
}
