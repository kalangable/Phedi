package com.phedi.domain.party.repository.item;

import java.util.List;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.PartyItem;
import com.phedi.domain.party.repository.PartyBaseRepository;

public interface PartyItemRepository<DOMAIN extends PartyItem> extends PartyBaseRepository<DOMAIN> {

    DOMAIN insertFor(DOMAIN item, Identifier partyIdentifier);

    List<DOMAIN> findAllByParty(Identifier partyIdentifier);
}