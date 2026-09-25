package com.phedi.infrastructure.persistence.party.entity.item;

import com.phedi.domain.party.model.item.ContactPurpose;
import com.phedi.domain.party.model.item.ContactType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Contato de um Party (e-mail, telefone, WhatsApp, ...).
 *
 * As colunas comuns (public_id, party_id, is_primary, is_active,
 * auditoria e soft-delete) vivem na tabela base {@code party_item}.
 */
@Entity
@Table(name = "party_contact")
@PrimaryKeyJoinColumn(name = "id")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PartyContactEntity extends PartyItemEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type", nullable = false, length = 30)
    private ContactType contactType;

    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", length = 20)
    private ContactPurpose purpose;

    @Column(name = "contact_value", nullable = false, length = 255)
    private String contactValue;

    @Column(name = "country_code", length = 2)
    private String countryCode;
}