package com.phedi.infrastructure.web.party.dto.item;

import java.util.Optional;

import lombok.Data;

/**
 * Espelho do PartyItem do domínio para updates parciais: campos opcionais e o
 * identifier (public_id do item existente) usado para casar com o registro —
 * ausente significa novo item.
 */
@Data
public abstract class PartyItemUpdateRequest {

    private Optional<String> identifier = Optional.empty();

    private Optional<Boolean> primary = Optional.empty();

}