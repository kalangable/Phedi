package com.phedi.application.party;

import java.util.List;

import com.phedi.domain.party.model.PartyBase;

public interface Listable<DOMAIN extends PartyBase> {

    List<DOMAIN> findAll();

}
