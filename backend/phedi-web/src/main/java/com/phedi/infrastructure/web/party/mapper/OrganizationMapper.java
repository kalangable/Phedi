package com.phedi.infrastructure.web.party.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.phedi.domain.party.model.Organization;
import com.phedi.infrastructure.web.party.dto.CreateOrganizationRequest;
import com.phedi.infrastructure.web.party.dto.OrganizationResponse;
import com.phedi.infrastructure.web.party.dto.UpdateOrganizationRequest;

/**
 * Conversao entre o payload de organizacao e o modelo de dominio
 * {@link Organization}.
 *
 * Os documentos aninhados no create delegam a {@link IdentityDocumentMapper},
 * para que "o que e um documento de identificacao" tenha um unico dono. Sem o
 * {@code uses}, o MapStruct sintetiza o mapeamento por conta propria e o
 * {@code isActive} dos documentos aninhados fica nulo, divergindo do que
 * acontece em {@code POST /parties/{id}/documents}.
 */
@Mapper(componentModel = "spring", uses = {PartyConversions.class, IdentityDocumentMapper.class})
public interface OrganizationMapper {

    /**
     * Os documentos aninhados sao apenas materialized aqui. Quem persiste cada
     * um e {@code OrganizationService}, que os registra na secao do agregado,
     * gera o identificador de cada e respeita a regra de primary.
     *
     * Contatos e enderecos sao aceitos pelo payload mas ainda nao persistidos:
     * nao existe repositorio para eles. O {@code ignore} e explicito de
     * proposito — sem ele o MapStruct sintetizaria um metodo que preenche as
     * listas e o descaste passaria despercebido. Quando a fatia de contato e
     * endereco existir, trocar por um {@code uses} para os mappers deles.
     */
    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    Organization toDomain(CreateOrganizationRequest dto);

    /**
     * O patch traz listas de contatos, enderecos e documentos, mas o update de
     * item tem ciclo de vida proprio (rota propria, validacao e regra de
     * primary) e nao esta implementado. Nenhum item aninhado e aplicado.
     */
    @Mapping(target = "identifier", source = "identifier")
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "documents", ignore = true)
    Organization toDomain(String identifier, UpdateOrganizationRequest dto);

    OrganizationResponse toDto(Organization domain);

}
