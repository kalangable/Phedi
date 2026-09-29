package com.phedi.infrastructure.persistence.party.entity;

import com.phedi.domain.party.model.PartyType;
import com.phedi.infrastructure.persistence.party.entity.base.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Raiz do Party Model: representa uma parte (pessoa física ou jurídica)
 * no agregado. A identidade, ativação e auditoria vêm de
 * {@link AbstractEntity}; aqui ficam apenas as colunas da tabela
 * base {@code party} e a estratégia de herança JOINED.
 */
@Entity
@Table(name = "party")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class PartyEntity extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "party_type", nullable = false, length = 20)
    private PartyType partyType;

    protected PartyEntity() {
    }

    protected PartyEntity(PartyType partyType) {
        this.partyType = partyType;
        setIsActive(true);
        setIsDeleted(false);
    }
}