package com.phedi.domain.party.repository.item;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.PartyIdentityDocument;

public interface PartyIdentityDocumentRepository extends PartyItemRepository<PartyIdentityDocument> {

    void demotePrimary(Identifier identifier);

}