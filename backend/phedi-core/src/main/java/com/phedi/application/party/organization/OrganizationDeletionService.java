package com.phedi.application.party.organization;

import com.phedi.domain.party.model.PartyIdentifier;

public interface OrganizationDeletionService {

        void delete(PartyIdentifier partyIdentifier);

}
