package com.phedi.domain.party.model.item;

import java.util.List;

import lombok.experimental.UtilityClass;

/**
 * Regra de negócio do agregado: cada Party pode ter no máximo um item
 * principal (primary) por seção — um documento principal, um contato
 * principal e um endereço principal.
 *
 * Aplicável a qualquer seção de itens via {@code <T extends PartyItem>}.
 */
@UtilityClass 
public final class PrimaryPolicy {

    public <T extends PartyItem> void ensureSinglePrimary(List<T> items) {
        long primaries = items.stream()
                .filter(item -> Boolean.TRUE.equals(item.getPrimary()))
                .count();
        if (primaries > 1) {
            throw new IllegalStateException(
                    "Apenas um item principal (primary) por Party é permitido.");
        }
    }
}