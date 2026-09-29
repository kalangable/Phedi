package com.phedi.domain.party.repository;

import java.util.List;

import com.phedi.domain.party.model.PartyBase;

public interface PartyRepository<DOMAIN extends PartyBase> extends PartyBaseRepository<DOMAIN> {
    
    DOMAIN insert(DOMAIN domain);

    List<DOMAIN> findAll();
}
