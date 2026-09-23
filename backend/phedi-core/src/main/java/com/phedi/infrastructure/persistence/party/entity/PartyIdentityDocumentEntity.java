package com.phedi.infrastructure.persistence.party.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.phedi.domain.party.model.DocumentType;

@Entity
@Table(name = "party_identity_document")
@Data
@NoArgsConstructor
public class PartyIdentityDocumentEntity {

    /**
     * Identificador técnico do registro na persistência.
     *
     * Não representa a identidade de negócio do Party.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador público do documento.
     *
     * Gerado por party_public_id_seq, a mesma sequence
     * usada por todas as tabelas do Party.
     * É o valor exposto externamente em URLs e APIs,
     * e não o id técnico acima.
     */
    @Column(name = "public_id", nullable = false, unique = true, length = 36)
    private String publicId;

    /**
     * Party ao qual este documento pertence.
     *
     * A relação utiliza o ID técnico da tabela party.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "party_id", nullable = false)
    private PartyEntity party;

    /**
     * Tipo do documento.
     *
     * Exemplos:
     * CPF, CNPJ, RG, PASSPORT, VAT,
     * STATE_TAX_REGISTRATION, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 30)
    private DocumentType documentType;

    /**
     * Número ou valor do documento.
     *
     * O valor deve ser armazenado de acordo com as regras
     * definidas para o tipo de documento.
     */
    @Column(name = "document_number", nullable = false, length = 100)
    private String documentNumber;

    /**
     * Código ISO 3166-1 alpha-2 do país relacionado ao documento.
     *
     * Exemplos: BR, US, AR.
     */
    @Column(name = "country_code", length = 2)
    private String countryCode;

    /**
     * Região, estado ou outra jurisdição relacionada à emissão.
     *
     * Pode ser relevante para documentos cuja identificação
     * depende da jurisdição, como inscrições estaduais.
     */
    @Column(name = "issuer_region", length = 10)
    private String issuerRegion;

    /**
     * Órgão ou autoridade responsável pela emissão do documento.
     */
    @Column(name = "issuing_authority", length = 100)
    private String issuingAuthority;

    /**
     * Data de emissão do documento.
     */
    @Column(name = "issued_at")
    private LocalDate issuedAt;

    /**
     * Data de expiração do documento, quando aplicável.
     */
    @Column(name = "expires_at")
    private LocalDate expiresAt;

    /**
     * Indica se este é o documento principal do Party.
     */
    @Column(name = "is_primary", nullable = false)
    private boolean primary;

    /**
     * Indica se o documento está ativo.
     *
     * É diferente de deletedAt:
     * um documento pode estar inativo sem ter sido
     * removido logicamente.
     */
    @Column(name = "is_active", nullable = false)
    private boolean active;

    /**
     * Data de criação do registro.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Data da última atualização.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Data da exclusão lógica.
     *
     * NULL significa que o registro não foi excluído logicamente.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
