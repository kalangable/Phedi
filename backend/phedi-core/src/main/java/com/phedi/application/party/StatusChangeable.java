package com.phedi.application.party;

import com.phedi.domain.party.model.Identifier;

public interface StatusChangeable {

    void activate(Identifier identifier);

    void deactivate(Identifier identifier);

}
