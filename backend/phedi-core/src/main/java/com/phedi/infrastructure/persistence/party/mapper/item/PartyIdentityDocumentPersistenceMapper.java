package com.phedi.infrastructure.persistence.party.mapper.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.phedi.domain.party.model.item.PartyIdentityDocument;
import com.phedi.infrastructure.persistence.party.entity.item.PartyIdentityDocumentEntity;

@Mapper(componentModel = "spring")
public interface PartyIdentityDocumentPersistenceMapper extends PartyItemPersistenceMapper<PartyIdentityDocument, PartyIdentityDocumentEntity> {

    @Override
    @Mapping(target = "publicId", source = "identifier.value")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "party", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "primary", defaultValue = "false")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    PartyIdentityDocumentEntity toEntity(PartyIdentityDocument domain);

    @Override
    @Mapping(target = "identifier", source = "publicId")
    PartyIdentityDocument toDomain(PartyIdentityDocumentEntity entity);

    /**
     * Aplica um patch parcial sobre a entidade JÁ CARREGADA do banco.
     *
     * Este método é escrito à mão de propósito. O {@code updateEntity} gerado
     * pelo MapStruct emite um setter por propriedade, sem guarda, e um patch de
     * um único campo zeraria todos os demais — como {@code identity_document_type}
     * e {@code identity_document_number} são {@code NOT NULL}, isso é violação
     * de constraint, não só perda de dado.
     *
     * Cada guarda compara com o valor atual da entidade, que já vem preenchido
     * (ver {@code AbstractPartyBaseRepository.update}). Campo ausente no corpo
     * do patch chega como null no domain e por isso fica preservado no banco.
     *
     * Consequência: não há como apagar um campo mandando null explícito. O
     * próprio {@code UpdateIdentityDocumentRequest} já não distingue null
     * explícito de campo ausente, porque ambos desserializam para
     * {@code Optional.empty}. Limpar um campo exigiria um sinal dedicado.
     *
     * Ao adicionar um campo em {@link PartyIdentityDocumentEntity}, inclua a
     * guarda correspondente aqui — este método não é gerado, então nada avisa
     * sobre omissão.
     */
    @Override
    default void updateEntity(PartyIdentityDocument domain, @MappingTarget PartyIdentityDocumentEntity entity) {
        if (domain.getCountryCode() != null) {
            entity.setCountryCode(domain.getCountryCode());
        }
        if (domain.getIssuerRegion() != null) {
            entity.setIssuerRegion(domain.getIssuerRegion());
        }
        if (domain.getIssuingAuthority() != null) {
            entity.setIssuingAuthority(domain.getIssuingAuthority());
        }
        if (domain.getIssuedAt() != null) {
            entity.setIssuedAt(domain.getIssuedAt());
        }
        if (domain.getExpiresAt() != null) {
            entity.setExpiresAt(domain.getExpiresAt());
        }
    }

}