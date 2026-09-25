package com.phedi.infrastructure.web.party.dto.item;

import lombok.Data;

/**
 * Espelho do PartyItem do domínio: base abstrata para os itens de um payload
 * de registro (contato, endereço, documento), compartilhando o campo primary.
 */
@Data
public abstract class PartyItemRequest {

    private Boolean primary;

}