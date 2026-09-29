package com.phedi.application.party.item;

import java.util.List;

import com.phedi.domain.party.model.Identifier;
import com.phedi.domain.party.model.item.PartyItem;

/**
 * Lista os itens de uma Party específica.
 *
 * Diferente de {@link com.phedi.application.party.Listable}, que lista sem
 * escopo. Um Item só existe no contexto da sua Party, então a identificação
 * da Party é obrigatória — não existe "todos os documentos" sem escopo.
 */
public interface PartyItemListable<DOMAIN extends PartyItem> {

    List<DOMAIN> findAllByParty(Identifier partyIdentifier);

}
