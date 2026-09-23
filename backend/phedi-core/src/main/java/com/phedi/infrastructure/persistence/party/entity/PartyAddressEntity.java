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
@Table(name = "party_address")
@Data
@NoArgsConstructor
public class PartyAddressEntity {

    /**
     * Identificador técnico do registro na persistência.
     *
     * Não representa a identidade de negócio do Party.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador público do endereço.
     *
     * Gerado por party_public_id_seq, a mesma sequence
     * usada por todas as tabelas do Party.
     * É o valor exposto externamente em URLs e APIs,
     * e não o id técnico acima.
     */
    @Column(name = "public_id", nullable = false, unique = true, length = 36)
    private String publicId;

    /**
     * Party ao qual este endereço pertence.
     *
     * A relação utiliza o ID técnico da tabela party.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "party_id", nullable = false)
    private PartyEntity party;

    /**
     * Tipo de endereço.
     *
     * Exemplos:
     * RESIDENTIAL, COMMERCIAL, BILLING,
     * SHIPPING, CORRESPONDENCE, OTHER.
     */
    @Column(name = "address_type", nullable = false, length = 20)
    private String addressType;

    /**
     * Apelido opcional para identificar o endereço.
     *
     * Exemplos: Casa, Escritório matriz, CAIXA postal.
     */
    @Column(name = "label", length = 50)
    private String label;

    /**
     * Logradouro (rua, avenida, travessa, ...).
     */
    @Column(name = "street", nullable = false, length = 255)
    private String street;

    /**
     * Número.
     *
     * NULL para endereços cuja jurisdição não usa numeração.
     */
    @Column(name = "number", length = 20)
    private String number;

    /**
     * Complemento (apartamento, sala, bloco, ...).
     */
    @Column(name = "complement", length = 100)
    private String complement;

    /**
     * Bairro/district.
     */
    @Column(name = "district", length = 100)
    private String district;

    /**
     * Cidade.
     */
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    /**
     * Estado, província ou região.
     *
     * Exemplos: SP, CA, Ontario.
     */
    @Column(name = "state_region", length = 100)
    private String stateRegion;

    /**
     * Código postal (CEP, ZIP, ...).
     *
     * NULL para países sem código postal.
     */
    @Column(name = "postal_code", length = 20)
    private String postalCode;

    /**
     * Código ISO 3166-1 alpha-2 do país.
     *
     * Exemplos: BR, US, AR.
     */
    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    /**
     * Indica se este é o endereço principal do Party.
     */
    @Column(name = "is_primary", nullable = false)
    private boolean primary;

    /**
     * Indica se o endereço está ativo.
     *
     * É diferente de deletedAt:
     * um endereço pode estar inativo sem ter sido
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
