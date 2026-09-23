package com.phedi.infrastructure.persistence.party.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "party_contact")
@Data
@NoArgsConstructor
public class PartyContactEntity {

    /**
     * Identificador técnico do registro na persistência.
     *
     * Não representa a identidade de negócio do Party.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador público do contato.
     *
     * Gerado por party_public_id_seq, a mesma sequence
     * usada por todas as tabelas do Party.
     * É o valor exposto externamente em URLs e APIs,
     * e não o id técnico acima.
     */
    @Column(name = "public_id", nullable = false, unique = true, length = 36)
    private String publicId;

    /**
     * Party ao qual este contato pertence.
     *
     * A relação utiliza o ID técnico da tabela party.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "party_id", nullable = false)
    private PartyEntity party;

    /**
     * Canal de contato.
     *
     * Exemplos:
     * EMAIL, PHONE, MOBILE, LANDLINE,
     * WHATSAPP, FAX, WEBSITE, OTHER.
     */
    @Column(name = "contact_type", nullable = false, length = 30)
    private String contactType;

    /**
     * Finalidade/contexto do contato.
     *
     * Exemplos:
     * PERSONAL, COMMERCIAL, BILLING, SUPPORT, OTHER.
     *
     * NULL significa que não há finalidade declarada.
     */
    @Column(name = "purpose", length = 20)
    private String purpose;

    /**
     * Valor do contato.
     *
     * Exemplos:
     * contato@empresa.com, +55 11 99999-9999.
     */
    @Column(name = "contact_value", nullable = false, length = 255)
    private String contactValue;

    /**
     * Código ISO 3166-1 alpha-2 do país do contato.
     *
     * Relevante para telefones com DDI.
     * Exemplos: BR, US, AR.
     */
    @Column(name = "country_code", length = 2)
    private String countryCode;

    /**
     * Indica se este é o contato principal do Party.
     */
    @Column(name = "is_primary", nullable = false)
    private boolean primary;

    /**
     * Indica se o contato está ativo.
     *
     * É diferente de deletedAt:
     * um contato pode estar inativo sem ter sido
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
