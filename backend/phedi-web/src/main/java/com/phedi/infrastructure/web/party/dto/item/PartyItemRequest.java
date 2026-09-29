package com.phedi.infrastructure.web.party.dto.item;

import java.util.Optional;

import lombok.Data;

@Data
public abstract class PartyItemRequest {

    private Boolean primary;

    public Optional<Boolean> getPrimary() {
        return Optional.ofNullable(primary);
    }

}