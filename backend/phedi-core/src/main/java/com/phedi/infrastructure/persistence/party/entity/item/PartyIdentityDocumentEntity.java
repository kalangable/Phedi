package com.phedi.infrastructure.persistence.party.entity.item;

import java.time.LocalDate;

import com.phedi.domain.party.model.item.DocumentType;

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
 * Documento de identificação de um Party (CPF, CNPJ, SSN, EIN, ...).
 *
 * As colunas comuns (public_id, party_id, is_primary, is_active,
 * auditoria e soft-delete) vivem na tabela base {@code party_item}.
 */
@Entity
@Table(name = "party_identity_document")
@PrimaryKeyJoinColumn(name = "id")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PartyIdentityDocumentEntity extends PartyItemEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 30)
    private DocumentType documentType;

    @Column(name = "document_number", nullable = false, length = 100)
    private String documentNumber;

    @Column(name = "country_code", length = 2)
    private String countryCode;

    @Column(name = "issuer_region", length = 10)
    private String issuerRegion;

    @Column(name = "issuing_authority", length = 100)
    private String issuingAuthority;

    @Column(name = "issued_at")
    private LocalDate issuedAt;

    @Column(name = "expires_at")
    private LocalDate expiresAt;
}