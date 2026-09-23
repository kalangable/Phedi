package com.phedi.domain.party.model;

public record Identifier(String value) {

    public Identifier {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Identifier cannot be empty");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}