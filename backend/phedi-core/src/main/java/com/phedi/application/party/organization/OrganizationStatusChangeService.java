package com.phedi.application.party.organization;

import com.phedi.domain.party.model.PartyIdentifier;

public interface OrganizationStatusChangeService {

    void activate(PartyIdentifier partyIdentifier);

    void deactivate(PartyIdentifier partyIdentifier);

}
