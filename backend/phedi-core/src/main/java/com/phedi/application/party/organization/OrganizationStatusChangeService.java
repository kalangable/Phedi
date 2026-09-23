package com.phedi.application.party.organization;

import com.phedi.domain.party.model.Identifier;

public interface OrganizationStatusChangeService {

    void activate(Identifier identifier);

    void deactivate(Identifier identifier);

}
