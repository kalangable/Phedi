package com.phedi.domain.party.model;

public record PartyIdentifier(String value) {

    public PartyIdentifier {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Party identifier cannot be empty");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
