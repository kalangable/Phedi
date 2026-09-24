package com.phedi.infrastructure.web.party.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Espelho do PartyItem do domínio para respostas: base abstrata dos itens com
 * identifier (public_id do item) e primary.
 */
@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class PartyItemResponse {

    private String identifier;

    private Boolean isActive;

    private Boolean primary;

}