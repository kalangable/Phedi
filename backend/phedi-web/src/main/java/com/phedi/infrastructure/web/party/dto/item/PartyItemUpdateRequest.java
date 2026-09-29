package com.phedi.infrastructure.web.party.dto.item;

import java.util.Optional;

import lombok.Data;

@Data
public abstract class PartyItemUpdateRequest {

    private String identifier;

    private Boolean primary;

    public Optional<String> getIdentifier() {
        return Optional.ofNullable(identifier);
    }

    public Optional<Boolean> getPrimary() {
        return Optional.ofNullable(primary);
    }
}