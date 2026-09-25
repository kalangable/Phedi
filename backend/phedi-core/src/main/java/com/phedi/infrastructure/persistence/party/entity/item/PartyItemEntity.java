package com.phedi.infrastructure.persistence.party.entity.item;

import com.phedi.infrastructure.persistence.party.entity.PartyEntity;
import com.phedi.infrastructure.persistence.party.entity.base.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Base dos itens do agregado Party (documentos, contatos e endereços).
 *
 * Espelha o desenho de {@code party -> organization/person}: a coluna
 * {@code party_id} (vínculo com o Party raiz), o marcador de item
 * principal {@code is_primary} e a estratégia JOINED. Os campos comuns
 * de identidade, ativação e auditoria vêm de {@link AbstractEntity}.
 */
@Entity
@Table(name = "party_item")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class PartyItemEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "party_id", nullable = false)
    private PartyEntity party;

    @Column(name = "is_primary", nullable = false)
    private Boolean primary = false;

    protected PartyItemEntity() {
    }
}