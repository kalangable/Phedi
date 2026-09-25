package com.phedi.application.party.item;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.PartyIdentityDocument;

public interface PartyDocumentCreationService {

    PartyIdentityDocument createDocument(Identifier partyIdentifier, PartyIdentityDocument document);

}